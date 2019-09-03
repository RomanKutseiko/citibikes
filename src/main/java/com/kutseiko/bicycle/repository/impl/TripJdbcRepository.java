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
import com.kutseiko.bicycle.repository.TripRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TripJdbcRepository implements TripRepository {

    private final DataSource dataSource;
    private final String SELECT_TRIPS = "SELECT * FROM " + TripTable.TABLE
            + " LEFT JOIN " + AppUserTable.TABLE + " ON " + TripTable.USER_ID + " = " + AppUserTable.TABLE + "." + AppUserTable.ID
            + " LEFT JOIN " + BikeTable.TABLE + " ON " + TripTable.BIKE_ID + " = " + BikeTable.TABLE + "." + BikeTable.ID
            + " LEFT JOIN " + StationTable.TABLE + " AS bs ON " + BikeTable.STATION_ID + " = bs." + StationTable.ID
            + " LEFT JOIN " + StationTable.TABLE + " AS ss ON " + TripTable.START_STATION_ID + " = ss." + StationTable.ID
            + " LEFT JOIN " + StationTable.TABLE + " AS es ON " + TripTable.END_STATION_ID + " = es." + StationTable.ID;

    @Override
    public Optional<Trip> getTripById(Long id) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(SELECT_TRIPS + " WHERE " + TripTable.TABLE + "." + TripTable.ID + "=?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return Optional.of(getTripFromRS(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Trip> getAllTrips() {
        List<Trip> Trips = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_TRIPS)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Trips.add(getTripFromRS(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Trips;
    }

    @Override
    public Optional<Trip> updateTrip(Trip trip) {
        String sql = "UPDATE " + TripTable.TABLE + " SET " + TripTable.USER_ID + "=?, " + TripTable.BIKE_ID + "=?, "
            + TripTable.START_STATION_ID + "=?," + TripTable.END_STATION_ID + "=?, " + TripTable.START_TIME + "=?, "
            + TripTable.END_TIME + "=? WHERE " + TripTable.ID + "=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, trip.getUser().getId());
            ps.setLong(2, trip.getBike().getId());
            ps.setLong(3, trip.getStartStation().getId());
            ps.setLong(4, trip.getEndStation().getId());
            ps.setTimestamp(5, Timestamp.valueOf(trip.getStartTime()));
            ps.setTimestamp(6, Timestamp.valueOf(trip.getEndTime()));
            ps.setLong(7, trip.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getTripById(trip.getId());
    }

    @Override
    public boolean deleteTripById(Long id) {
        String sql = "DELETE FROM " + TripTable.TABLE + " WHERE " + TripTable.ID + "=?";
        boolean deleted = false;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            deleted = ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deleted;
    }

    @Override
    public Optional<Trip> addTrip(Trip trip) {
        String sql = "INSERT INTO Trip(user_id, bike_id, start_station_id, end_station_id, start_time, end_time) "
            + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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
            e.printStackTrace();
        }
        return getTripById(trip.getId());
    }

    private Trip getTripFromRS(ResultSet rs) throws SQLException {
        return new Trip()
                .setId(rs.getLong(1))
                .setStartTime(rs.getTimestamp("start_time").toLocalDateTime())
                .setEndTime(rs.getTimestamp("end_time").toLocalDateTime())
                .setUser(new User().setId(rs.getLong(8))
                        .setDateOfBirth(convertDateToLocalDate(rs.getDate("birthday")))
                        .setEmail(rs.getString("email"))
                        .setGender(Gender.valueOf(rs.getString("gender")))
                        .setUserType(UserType.valueOf(rs.getString("user_type"))))
                .setBike(new Bike()
                        .setId(rs.getLong(13))
                        .setInfo(rs.getString("info"))
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
