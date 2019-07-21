package com.kutseiko.bicycle.repository.impl;

import com.kutseiko.bicycle.entity.Bike;
import com.kutseiko.bicycle.entity.Station;
import com.kutseiko.bicycle.repository.BikeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
public class BikeJdbcRepository implements BikeRepository {

    private final DataSource dataSource;

    @Override
    public Optional<Bike> getBikeById(Long id) {
        String sql = "SELECT * FROM bike LEFT JOIN station ON station_id = station.id  WHERE bike.id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return Optional.of(new Bike()
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
        return Optional.empty();
    }

    @Override
    public List<Bike> getAllBikes() {
        String sql = "SELECT * FROM bike LEFT JOIN station ON station_id = station.id";
        List<Bike> bikes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bikes.add(new Bike()
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
        return bikes;
    }

    @Override
    public Optional<Bike> updateBike(Bike bike) {
        String sql = "UPDATE bike SET station_id=?, info=? WHERE id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, bike.getStation().getId());
            ps.setString(2, bike.getInfo());
            ps.setLong(3, bike.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(bike);
    }

    @Override
    public boolean deleteBikeById(Long id) {
        String sql = "DELETE FROM bike WHERE id=?";
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
    public Optional<Bike> addBike(Bike bike) {
        String sql = "INSERT INTO bike(station_id, info) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, bike.getStation().getId());
            ps.setString(2, bike.getInfo());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                bike.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(bike);
    }

}
