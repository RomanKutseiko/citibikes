package com.kutseiko.bicycle.repository.impl;

import static com.kutseiko.bicycle.utils.DateConverter.convertDateToLocalDate;
import static com.kutseiko.bicycle.utils.DateConverter.convertLocalDateToDate;

import com.kutseiko.bicycle.core.type.Gender;
import com.kutseiko.bicycle.core.type.UserType;
import com.kutseiko.bicycle.core.type.db.tables.AppUserTable;
import com.kutseiko.bicycle.entity.User;
import com.kutseiko.bicycle.exception.CustomSQLException;
import com.kutseiko.bicycle.exception.EntityNotFoundException;
import com.kutseiko.bicycle.repository.UserRepository;
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
public class UserJdbcRepository implements UserRepository {

    private final DataSource dataSource;
    private static String getAllSql;
    private static String getByIdSql;
    private static String updateSql;
    private static String deletedSql;
    private static String addSql;

    static {
        StringBuilder sb = new StringBuilder();
        getAllSql = sb.append("SELECT * FROM ").append(AppUserTable.TABLE).toString();
        getByIdSql = sb.append(" WHERE ").append(AppUserTable.ID).append("=?").toString();
        updateSql = new StringBuilder().append("UPDATE ").append(AppUserTable.TABLE).append(" SET ").append(AppUserTable.BIRTHDAY)
            .append("=?, ").append(AppUserTable.EMAIL).append("=?, ").append(AppUserTable.GENDER).append("=?::GENDER_ENUM, ")
            .append(AppUserTable.USER_TYPE).append("=?::USER_TYPE_ENUM WHERE ").append(AppUserTable.ID).append("=?").toString();
        deletedSql = new StringBuilder().append("DELETE FROM ").append(AppUserTable.TABLE).append(" WHERE ").append(AppUserTable.ID)
            .append("=?").toString();
        addSql = new StringBuilder().append("INSERT INTO ").append(AppUserTable.TABLE).append("(").append(AppUserTable.BIRTHDAY)
            .append(", ").append(AppUserTable.EMAIL).append(", ").append(AppUserTable.GENDER).append(", ").append(AppUserTable.USER_TYPE)
            .append(") VALUES (?, ?, ?::GENDER_ENUM, ?::USER_TYPE_ENUM)").toString();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        log.debug(getByIdSql);
        Optional<User> result = Optional.empty();
        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(getByIdSql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapUserFromRS(rs));
            } else {
                throw new EntityNotFoundException();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new CustomSQLException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        log.debug(getAllSql);
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(getAllSql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(mapUserFromRS(rs));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new CustomSQLException(e);
        }
        return users;
    }

    @Override
    public Optional<User> updateUser(User user) {
        log.debug(updateSql);
        int rowsAffected;
        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(updateSql)) {
            ps.setDate(1, convertLocalDateToDate(user.getDateOfBirth()));
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getGender().getName());
            ps.setString(4, user.getUserType().getName());
            ps.setLong(5, user.getId());
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new CustomSQLException(e);
        }

        if (rowsAffected > 0) {
            return getUserById(user.getId());
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public boolean deleteUserById(Long id) {
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
    public Optional<User> addUser(User user) {
        log.debug(addSql);
        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(addSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, convertLocalDateToDate(user.getDateOfBirth()));
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getGender().getName());
            ps.setString(4, user.getUserType().getName());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new CustomSQLException(e);
        }
        return getUserById(user.getId());
    }

    private User mapUserFromRS(ResultSet rs) throws SQLException {
        return new User().setId(rs.getLong(AppUserTable.ID))
            .setDateOfBirth(convertDateToLocalDate(rs.getDate(AppUserTable.BIRTHDAY)))
            .setEmail(rs.getString(AppUserTable.EMAIL))
            .setGender(Gender.valueOf(rs.getString(AppUserTable.GENDER)))
            .setUserType(UserType.valueOf(rs.getString(AppUserTable.USER_TYPE)));
    }
}
