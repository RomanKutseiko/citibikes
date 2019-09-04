package com.kutseiko.bicycle.repository.impl;

import com.kutseiko.bicycle.core.type.db.tables.BikeTable;
import com.kutseiko.bicycle.core.type.db.tables.StationTable;
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
        String sql = "SELECT * FROM " + BikeTable.TABLE + " LEFT JOIN " + StationTable.TABLE + " ON " + BikeTable.STATION_ID + " = "
            + StationTable.TABLE + "." + StationTable.ID + " WHERE " + BikeTable.TABLE + "." + BikeTable.ID + "=?";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return Optional.of(mapBikeFromRS(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Bike> getAllBikes() {
        String sql = "SELECT * FROM " + BikeTable.TABLE + " LEFT JOIN " + StationTable.TABLE + " ON " + BikeTable.STATION_ID + " = "
            + StationTable.TABLE + "." + StationTable.ID;
        List<Bike> bikes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bikes.add(mapBikeFromRS(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bikes;
    }

    @Override
    public Optional<Bike> updateBike(Bike bike) {
        String sql = "UPDATE " + BikeTable.TABLE + " SET " + BikeTable.STATION_ID + "=?, " + BikeTable.INFO + "=? WHERE "+ BikeTable.ID + "=?";
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
        String sql = "DELETE FROM " + BikeTable.TABLE + " WHERE " + BikeTable.ID + "=?";
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
        String sql = "INSERT INTO " + BikeTable.TABLE + "(" + BikeTable.STATION_ID + ", " + BikeTable.INFO + ") VALUES (?, ?)";
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

    private Bike mapBikeFromRS(ResultSet rs) throws SQLException {
        return new Bike()
            .setId(rs.getLong(BikeTable.ID))
            .setInfo(rs.getString(BikeTable.INFO))
            .setStation(new Station()
                .setId(rs.getLong(BikeTable.STATION_ID))
                .setName(rs.getString(StationTable.NAME))
                .setLatitude(rs.getDouble(StationTable.LATITUDE))
                .setLongitude(rs.getDouble(StationTable.LONGITUDE)));
    }

}
