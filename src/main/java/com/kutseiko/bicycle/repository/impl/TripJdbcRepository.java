package com.kutseiko.bicycle.repository.impl;

import static com.kutseiko.bicycle.utils.DateConverter.convertDateToLocalDate;

import com.kutseiko.bicycle.core.type.Gender;
import com.kutseiko.bicycle.core.type.UserType;
import com.kutseiko.bicycle.core.type.db.tables.AppUserTable;
import com.kutseiko.bicycle.core.type.db.tables.BikeTable;
import com.kutseiko.bicycle.core.type.db.tables.StationTable;
import com.kutseiko.bicycle.core.type.db.tables.TripTable;
import com.kutseiko.bicycle.entity.Bike;
import com.kutseiko.bicycle.entity.Trip;
import com.kutseiko.bicycle.entity.Station;
import com.kutseiko.bicycle.entity.User;
import com.kutseiko.bicycle.exception.CustomSQLException;
import com.kutseiko.bicycle.repository.TripRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TripJdbcRepository implements TripRepository {

    private final DataSource dataSource;
    private static String getAllSql;
    private static String getByIdSql;
    private static String updateSql;
    private static String deletedSql;
    private static String addSql;
    static {
        StringBuilder sb = new StringBuilder();
        getAllSql = sb.append("SELECT * FROM ").append(TripTable.TABLE).append(" LEFT JOIN ").append(AppUserTable.TABLE).append(" ON ")
            .append(TripTable.USER_ID).append(" = ").append(AppUserTable.TABLE).append(".").append(AppUserTable.ID).append(" LEFT JOIN ")
            .append(BikeTable.TABLE).append(" ON ").append(TripTable.BIKE_ID).append(" = ").append(BikeTable.TABLE).append(".")
            .append(BikeTable.ID).append(" LEFT JOIN ").append(StationTable.TABLE).append(" AS bs ON ").append(BikeTable.STATION_ID)
            .append(" = bs.").append(StationTable.ID).append(" LEFT JOIN ").append(StationTable.TABLE).append(" AS ss ON ")
            .append(TripTable.START_STATION_ID).append(" = ss.").append(StationTable.ID).append(" LEFT JOIN ").append(StationTable.TABLE)
            .append(" AS es ON ").append(TripTable.END_STATION_ID).append(" = es.").append(StationTable.ID).toString();
        getByIdSql = sb.append(" WHERE ").append(TripTable.TABLE).append(".").append(TripTable.ID).append("=?").toString();
        updateSql = new StringBuilder().append("UPDATE ").append(TripTable.TABLE).append(" SET ").append(TripTable.USER_ID).append("=?, ")
            .append(TripTable.BIKE_ID).append("=?, ").append(TripTable.START_STATION_ID).append("=?,").append(TripTable.END_STATION_ID)
            .append("=?, ").append(TripTable.START_TIME).append("=?, ").append(TripTable.END_TIME).append("=? WHERE ").append(TripTable.ID)
            .append("=?").toString();
        deletedSql = new StringBuilder().append("DELETE FROM ").append(TripTable.TABLE).append(" WHERE ").append(TripTable.ID)
            .append("=?").toString();
        addSql = new StringBuilder().append("INSERT INTO ").append(TripTable.TABLE).append("(").append(TripTable.USER_ID).append(", ")
            .append(TripTable.BIKE_ID).append(", ").append(TripTable.START_STATION_ID).append(", ").append(TripTable.END_STATION_ID)
            .append(", ").append(TripTable.START_TIME).append(", ").append(TripTable.END_TIME).append(") ")
            .append("VALUES (?, ?, ?, ?, ?, ?)").toString();
    }

    @Override
    public Optional<Trip> getTripById(Long id) {
        log.debug(getByIdSql);
        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(getByIdSql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return Optional.of(getTripFromRS(rs));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new CustomSQLException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Trip> getAllTrips() {
        log.debug(getAllSql);
        List<Trip> Trips = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(getAllSql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Trips.add(getTripFromRS(rs));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new CustomSQLException(e);
        }
        return Trips;
    }

    @Override
    public Optional<Trip> updateTrip(Trip trip) {
        log.debug(updateSql);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(updateSql)) {
            ps.setLong(1, trip.getUser().getId());
            ps.setLong(2, trip.getBike().getId());
            ps.setLong(3, trip.getStartStation().getId());
            ps.setLong(4, trip.getEndStation().getId());
            ps.setTimestamp(5, Timestamp.valueOf(trip.getStartTime()));
            ps.setTimestamp(6, Timestamp.valueOf(trip.getEndTime()));
            ps.setLong(7, trip.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new CustomSQLException(e);
        }
        return getTripById(trip.getId());
    }

    @Override
    public boolean deleteTripById(Long id) {
        log.debug(deletedSql);
        boolean deleted = false;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(deletedSql)) {
            ps.setLong(1, id);
            deleted = ps.executeUpdate() == 1;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new CustomSQLException(e);
        }
        return deleted;
    }

    @Override
    public Optional<Trip> addTrip(Trip trip) {
        log.debug(addSql);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(addSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, trip.getUser().getId());
            ps.setLong(2, trip.getBike().getId());
            ps.setLong(3, trip.getStartStation().getId());
            ps.setLong(4, trip.getEndStation().getId());
            ps.setTimestamp(5, Timestamp.valueOf(trip.getStartTime()));
            ps.setTimestamp(6, Timestamp.valueOf(trip.getEndTime()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                trip.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new CustomSQLException(e);
        }
        return getTripById(trip.getId());
    }

    private Trip getTripFromRS(ResultSet rs) throws SQLException {
        return new Trip()
                .setId(rs.getLong(1))
                .setStartTime(rs.getTimestamp(TripTable.START_TIME).toLocalDateTime())
                .setEndTime(rs.getTimestamp(TripTable.END_TIME).toLocalDateTime())
                .setUser(new User().setId(rs.getLong(8))
                        .setDateOfBirth(convertDateToLocalDate(rs.getDate(AppUserTable.BIRTHDAY)))
                        .setEmail(rs.getString(AppUserTable.EMAIL))
                        .setGender(Gender.valueOf(rs.getString(AppUserTable.GENDER)))
                        .setUserType(UserType.valueOf(rs.getString(AppUserTable.USER_TYPE))))
                .setBike(new Bike()
                        .setId(rs.getLong(13))
                        .setInfo(rs.getString(BikeTable.INFO))
                        .setStation(new Station()
                                .setId(rs.getLong(14))
                                .setName(rs.getString(17))
                                .setLatitude(rs.getDouble(18))
                                .setLongitude(rs.getDouble(19))))
               .setStartStation(new Station()
                        .setId(rs.getLong(20))
                        .setName(rs.getString(21))
                        .setLatitude(rs.getDouble(22))
                        .setLongitude(rs.getDouble(23)))
                .setEndStation(new Station()
                        .setId(rs.getLong(24))
                        .setName(rs.getString(25))
                        .setLatitude(rs.getDouble(26))
                        .setLongitude(rs.getDouble(27)));
    }

}
