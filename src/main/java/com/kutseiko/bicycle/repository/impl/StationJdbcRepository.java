package com.kutseiko.bicycle.repository.impl;

import com.kutseiko.bicycle.core.type.db.tables.StationTable;
import com.kutseiko.bicycle.entity.Station;
import com.kutseiko.bicycle.exception.CustomSQLException;
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
import org.postgresql.geometric.PGpoint;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
@Slf4j
public class StationJdbcRepository implements StationRepository {

    private final DataSource dataSource;
    private static String getAllSql;
    private static String getByIdSql;
    private static String updateSql;
    private static String deletedSql;
    private static String addSql;

    static {
        StringBuilder sb = new StringBuilder();
        getAllSql = sb.append("SELECT * FROM ").append(StationTable.TABLE).toString();
        getByIdSql = sb.append(" WHERE ").append(StationTable.ID).append("=?").toString();
        updateSql = new StringBuilder().append("UPDATE ").append(StationTable.TABLE).append(" SET ").append(StationTable.NAME)
            .append("=?, ").append(StationTable.COORDINATES).append("=POINT(?, ?) ").append(" WHERE ")
            .append(StationTable.ID).append("=?").toString();
        deletedSql = new StringBuilder().append("DELETE FROM ").append(StationTable.TABLE).append(" WHERE ").append(StationTable.ID)
            .append("=?").toString();
        addSql = new StringBuilder().append("INSERT INTO ").append(StationTable.TABLE).append("(").append(StationTable.NAME).append(", ")
            .append(StationTable.COORDINATES).append(") VALUES (?, POINT(?, ?))").toString();
    }

    @Override
    public Optional<Station> getStationById(Long id) {
        log.debug(getByIdSql);
        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(getByIdSql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return Optional.of(mapStationFromRS(rs));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new CustomSQLException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Station> getAllStations() {
        log.debug(getAllSql);
        List<Station> stations = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(getAllSql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                stations.add(mapStationFromRS(rs));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new CustomSQLException(e);
        }
        return stations;
    }

    @Override
    public Optional<Station> updateStation(Station station) {
        log.debug(updateSql);
        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(updateSql)) {
            ps.setString(1, station.getName());
            ps.setDouble(2, station.getLongitude());
            ps.setDouble(3, station.getLatitude());
            ps.setLong(4, station.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new CustomSQLException(e);
        }
        return getStationById(station.getId());
    }

    @Override
    public boolean deleteStationById(Long id) {
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
    public Optional<Station> addStation(Station station) {
        log.debug(addSql);
        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(addSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, station.getName());
            ps.setDouble(2, station.getLongitude());
            ps.setDouble(3, station.getLatitude());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                station.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new CustomSQLException(e);
        }
        return getStationById(station.getId());
    }

    private Station mapStationFromRS(ResultSet rs) throws SQLException {
        return new Station().setId(rs.getLong(StationTable.ID)).setLongitude(((PGpoint) rs.getObject("coordinates")).x)
            .setLatitude(((PGpoint) rs.getObject("coordinates")).y).setName(rs.getString(StationTable.NAME));
    }

}
