package com.kutseiko.bicycle.repository.impl;

import com.kutseiko.bicycle.entity.Station;
import com.kutseiko.bicycle.repository.StationRepository;
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
public class StationJdbcRepository implements StationRepository {

    private final Connection connection;

    @Override
    public Optional<Station> getStationById(Long id) {
        String getStationById = "SELECT * FROM station WHERE id=?";
        try {
            PreparedStatement ps = connection.prepareStatement(getStationById);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return Optional.of(new Station().setId(rs.getLong("id")).setLatitude(rs.getDouble("latitude"))
                    .setLongitude(rs.getDouble("longitude")).setName(rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Station> getAllStations() {
        String getStations = "SELECT * FROM station";
        List<Station> stations = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(getStations);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                stations.add(new Station().setId(rs.getLong("id")).setLatitude(rs.getDouble("latitude"))
                    .setLongitude(rs.getDouble("longitude")).setName(rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stations;
    }

    @Override
    public Optional<Station> updateStation(Station station) {
        String updateStationById = "UPDATE station SET name=?, latitude=?, longitude=? WHERE id=?";
        try {
            PreparedStatement ps = connection.prepareStatement(updateStationById);
            ps.setString(1, station.getName());
            ps.setDouble(2, station.getLatitude());
            ps.setDouble(3, station.getLongitude());
            ps.setLong(4, station.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(station);
    }

    @Override
    public boolean deleteStationById(Long id) {
        String deleteStationById = "DELETE FROM station WHERE id=?";
        boolean deleted = false;
        try {
            PreparedStatement ps = connection.prepareStatement(deleteStationById);
            ps.setLong(1, id);
            deleted = ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deleted;
    }

    @Override
    public Optional<Station> addStation(Station station) {
        String addStation = "INSERT INTO station(name, latitude, longitude) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(addStation, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, station.getName());
            ps.setDouble(2, station.getLatitude());
            ps.setDouble(3, station.getLongitude());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                station.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(station);
    }

}
