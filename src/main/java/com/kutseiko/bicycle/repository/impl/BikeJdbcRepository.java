package com.kutseiko.bicycle.repository.impl;

import com.kutseiko.bicycle.entity.Bike;
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

@Component
@RequiredArgsConstructor
public class BikeJdbcRepository implements BikeRepository {

    private final Connection connection;

    @Override
    public Optional<Bike> getBikeById(Long id) {
        String getBikeById = "SELECT * FROM bike WHERE id=?";
        try {
            PreparedStatement ps = connection.prepareStatement(getBikeById);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return Optional.of(new Bike().setId(rs.getLong("id")).setInfo(rs.getString("info")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Bike> getAllBikes() {
        String getBikeById = "SELECT * FROM bike";
        List<Bike> bikes = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(getBikeById);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bikes.add(new Bike().setId(rs.getLong("id")).setInfo(rs.getString("info")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bikes;
    }

    @Override
    public Optional<Bike> updateBike(Bike bike) {
        String getBikeById = "UPDATE bike SET station_id=?, info=? WHERE id=?";
        try {
            PreparedStatement ps = connection.prepareStatement(getBikeById);
            ps.setLong(1, bike.getStationId());
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
        String getBikeById = "DELETE FROM bike WHERE id=?";
        boolean deleted = false;
        try {
            PreparedStatement ps = connection.prepareStatement(getBikeById);
            ps.setLong(1, id);
            deleted = ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deleted;
    }

    @Override
    public Optional<Bike> addBike(Bike bike) {
        String getBikeById = "INSERT INTO bike(station_id, info) VALUES (?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(getBikeById, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, bike.getStationId());
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
