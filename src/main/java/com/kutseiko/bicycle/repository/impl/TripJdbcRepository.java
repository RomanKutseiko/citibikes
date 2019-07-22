package com.kutseiko.bicycle.repository.impl;

import static com.kutseiko.bicycle.utils.DateConverter.convertDateToLocalDate;

import com.kutseiko.bicycle.entity.Bike;
import com.kutseiko.bicycle.entity.Trip;
import com.kutseiko.bicycle.entity.Station;
import com.kutseiko.bicycle.repository.TripRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    @Override
    public Optional<Trip> getTripById(Long id) {
        String sql = "SELECT * FROM trip "
            + "LEFT JOIN station ON start_station_id = station.id  "
            + "LEFT JOIN station ON end_station_id = station.id  "
            + "LEFT JOIN appuser ON user_id = appuser.id  "
            + "LEFT JOIN bike ON bike_id = bike.id  "
            + "WHERE trip.id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return Optional.of(new Trip()
                    .setId(rs.getLong(1))
                    .setStartTime(rs.getTimestamp("start_time").toLocalDateTime())
                    .setEndTime(rs.getTimestamp("end_time").toLocalDateTime())
                    .setBike(new Bike()
                        .setId(rs.getLong("id"))
                        .setInfo(rs.getString("info"))
                        .setStation(new Station()
                            .setId(rs.getLong("station_id"))
                            .setName(rs.getString("name"))
                            .setLatitude(rs.getDouble("latitude"))
                            .setLongitude(rs.getDouble("longitude"))))
                    .setStartStation(new Station()
                        .setId(rs.getLong(4))
                        .setName(rs.getString("name"))
                        .setLatitude(rs.getDouble("latitude"))
                        .setLongitude(rs.getDouble("longitude")))
                    .setEndStation(new Station()
                        .setId(rs.getLong(5))
                        .setName(rs.getString("name"))
                        .setLatitude(rs.getDouble("latitude"))
                        .setLongitude(rs.getDouble("longitude"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Trip> getAllTrips() {
        String sql = "SELECT * FROM Trip LEFT JOIN station ON station_id = station.id";
        List<Trip> Trips = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Trips.add(new Trip()
                    .setId(rs.getLong("id"))
                    .setInfo(rs.getString("info"))
                    .setStation(new Station()
                        .setId(rs.getLong("station_id"))
                        .setName(rs.getString("name"))
                        .setLatitude(rs.getDouble("latitude"))
                        .setLongitude(rs.getDouble("longitude"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Trips;
    }

    @Override
    public Optional<Trip> updateTrip(Trip Trip) {
        String sql = "UPDATE Trip SET station_id=?, info=? WHERE id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, Trip.getStation().getId());
            ps.setString(2, Trip.getInfo());
            ps.setLong(3, Trip.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(Trip);
    }

    @Override
    public boolean deleteTripById(Long id) {
        String sql = "DELETE FROM Trip WHERE id=?";
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
    public Optional<Trip> addTrip(Trip Trip) {
        String sql = "INSERT INTO Trip(station_id, info) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, Trip.getStation().getId());
            ps.setString(2, Trip.getInfo());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                Trip.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(Trip);
    }

}
