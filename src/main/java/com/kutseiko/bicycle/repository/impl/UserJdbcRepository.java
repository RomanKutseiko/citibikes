package com.kutseiko.bicycle.repository.impl;

import static com.kutseiko.bicycle.utils.DateConverter.convertDateToLocalDate;
import static com.kutseiko.bicycle.utils.DateConverter.convertLocalDateToDate;

import com.kutseiko.bicycle.core.type.Gender;
import com.kutseiko.bicycle.core.type.UserType;
import com.kutseiko.bicycle.core.type.db.tables.AppUserTable;
import com.kutseiko.bicycle.entity.User;
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
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
public class UserJdbcRepository implements UserRepository {

    private final DataSource dataSource;

    @Override
    public Optional<User> getUserById(Long id) {
        String sql = "SELECT * FROM " + AppUserTable.TABLE + " WHERE " + AppUserTable.ID + "=?";
        Optional<User> result = Optional.empty();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result = Optional.of(mapUserFromRS(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM " + AppUserTable.TABLE;
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(mapUserFromRS(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Optional<User> updateUser(User user) {
        String sql = "UPDATE " + AppUserTable.TABLE + " SET " + AppUserTable.BIRTHDAY + "=?, " + AppUserTable.EMAIL + "=?, "
            + AppUserTable.GENDER + "=?::GENDER_ENUM, " + AppUserTable.USER_TYPE + "=?::USER_TYPE_ENUM WHERE " + AppUserTable.ID + "=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, convertLocalDateToDate(user.getDateOfBirth()));
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getGender().getName());
            ps.setString(4, user.getUserType().getName());
            ps.setLong(5, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(user);
    }

    @Override
    public boolean deleteUserById(Long id) {
        String sql = "DELETE FROM " + AppUserTable.TABLE + " WHERE " + AppUserTable.ID + "=?";
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
    public Optional<User> addUser(User user) {
        String sql = "INSERT INTO " + AppUserTable.TABLE + "(" + AppUserTable.BIRTHDAY + ", " + AppUserTable.EMAIL + ", "
            + AppUserTable.GENDER + ", " + AppUserTable.USER_TYPE + ") VALUES (?, ?, ?::GENDER_ENUM, ?::USER_TYPE_ENUM)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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
            e.printStackTrace();
        }
        return Optional.of(user);
    }

    private User mapUserFromRS(ResultSet rs) throws SQLException {
        return new User().setId(rs.getLong(AppUserTable.ID))
            .setDateOfBirth(convertDateToLocalDate(rs.getDate(AppUserTable.BIRTHDAY)))
            .setEmail(rs.getString(AppUserTable.EMAIL))
            .setGender(Gender.valueOf(rs.getString(AppUserTable.GENDER)))
            .setUserType(UserType.valueOf(rs.getString(AppUserTable.USER_TYPE)));
    }
}
