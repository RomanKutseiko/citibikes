package com.kutseiko.bicycle.repository.impl;

import com.kutseiko.bicycle.core.type.db.tables.BikeTable;
import com.kutseiko.bicycle.core.type.db.tables.StationTable;
import com.kutseiko.bicycle.entity.Bike;
import com.kutseiko.bicycle.entity.Station;
import com.kutseiko.bicycle.exception.CustomSQLException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
@Slf4j
public class BikeJdbcRepository implements BikeRepository {

    private final DataSource dataSource;
    private static String getAllSql;
    private static String getByIdSql;
    private static String updateSql;
    private static String deletedSql;
    private static String addSql;
    static {
        StringBuilder sb = new StringBuilder();
        getAllSql = sb.append("SELECT * FROM ").append(BikeTable.TABLE).append(" LEFT JOIN ").append(StationTable.TABLE).append(" ON ")
            .append(BikeTable.STATION_ID).append(" = ").append(StationTable.TABLE).append(".").append(StationTable.ID).toString();
        getByIdSql = sb.append(" WHERE ").append(BikeTable.TABLE).append(".").append(BikeTable.ID + "=?").toString();
        updateSql = new StringBuilder().append("UPDATE ").append(BikeTable.TABLE).append(" SET ").append(BikeTable.STATION_ID)
            .append("=?, ").append(BikeTable.INFO).append("=? WHERE ").append(BikeTable.ID).append("=?").toString();
        deletedSql = new StringBuilder().append("DELETE FROM ").append(BikeTable.TABLE).append(" WHERE ").append(BikeTable.ID)
            .append("=?").toString();
        addSql = new StringBuilder().append("INSERT INTO ").append(BikeTable.TABLE).append("(").append(BikeTable.STATION_ID).append(", ")
            .append(BikeTable.INFO).append(") VALUES (?, ?)").toString();
    }

    @Override
    public Optional<Bike> getBikeById(Long id) {
        log.debug(getByIdSql);
        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(getByIdSql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return Optional.of(mapBikeFromRS(rs));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new CustomSQLException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Bike> getAllBikes() {
        log.debug(getAllSql);
        List<Bike> bikes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(getAllSql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bikes.add(mapBikeFromRS(rs));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new CustomSQLException(e);
        }
        return bikes;
    }

    @Override
    public Optional<Bike> updateBike(Bike bike) {
        log.debug(updateSql);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(updateSql)) {
            ps.setLong(1, bike.getStation().getId());
            ps.setString(2, bike.getInfo());
            ps.setLong(3, bike.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new CustomSQLException(e);
        }
        return Optional.of(bike);
    }

    @Override
    public boolean deleteBikeById(Long id) {
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
    public Optional<Bike> addBike(Bike bike) {
        log.debug(addSql);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(addSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, bike.getStation().getId());
            ps.setString(2, bike.getInfo());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                bike.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new CustomSQLException(e);
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
