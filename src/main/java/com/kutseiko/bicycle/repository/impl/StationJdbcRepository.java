package com.kutseiko.bicycle.repository.impl;

import com.kutseiko.bicycle.core.type.db.tables.StationTable;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
@Slf4j
public class StationJdbcRepository implements StationRepository {

    private final DataSource dataSource;

    @Override
    public Optional<Station> getStationById(Long id) {
        String sql = "SELECT * FROM " + StationTable.TABLE + " WHERE " + StationTable.ID + "=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return Optional.of(mapStationFromRS(rs));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Station> getAllStations() {
        String sql = "SELECT * FROM " + StationTable.TABLE;
        List<Station> stations = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                stations.add(mapStationFromRS(rs));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return stations;
    }

    @Override
    public Optional<Station> updateStation(Station station) {
        String sql = "UPDATE " + StationTable.TABLE + " SET " + StationTable.NAME + "=?, " + StationTable.LATITUDE + "=?, "
            + StationTable.LONGITUDE + "=? WHERE " + StationTable.ID + "=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, station.getName());
            ps.setDouble(2, station.getLatitude());
            ps.setDouble(3, station.getLongitude());
            ps.setLong(4, station.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return Optional.of(station);
    }

    @Override
    public boolean deleteStationById(Long id) {
        String sql = "DELETE FROM " + StationTable.TABLE + " WHERE " + StationTable.ID + "=?";
        boolean deleted = false;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            deleted = ps.executeUpdate() == 1;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return deleted;
    }

    @Override
    public Optional<Station> addStation(Station station) {
        String sql = "INSERT INTO " + StationTable.TABLE + "(" + StationTable.NAME + ", " + StationTable.LATITUDE + ", "
            + StationTable.LONGITUDE + ") VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, station.getName());
            ps.setDouble(2, station.getLatitude());
            ps.setDouble(3, station.getLongitude());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                station.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return Optional.of(station);
    }

    private Station mapStationFromRS(ResultSet rs) throws SQLException {
        return new Station().setId(rs.getLong(StationTable.ID)).setLatitude(rs.getDouble(StationTable.LATITUDE))
            .setLongitude(rs.getDouble(StationTable.LONGITUDE)).setName(rs.getString(StationTable.NAME));
    }

}
