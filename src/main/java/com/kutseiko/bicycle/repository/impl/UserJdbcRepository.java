package com.kutseiko.bicycle.repository.impl;

import static com.kutseiko.bicycle.utils.DateConverter.convertDateToLocalDate;
import static com.kutseiko.bicycle.utils.DateConverter.convertLocalDateToDate;

import com.kutseiko.bicycle.core.type.Gender;
import com.kutseiko.bicycle.core.type.UserType;
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
        String sql = "SELECT * FROM appuser WHERE id=?";
        User user = new User();
        Optional<User> result = Optional.empty();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result = Optional.of(user
                    .setId(rs.getLong("id"))
                    .setDateOfBirth(convertDateToLocalDate(rs.getDate("birthday")))
                    .setEmail(rs.getString("email"))
                    .setGender(Gender.valueOf(rs.getInt("gender")))
                    .setUserType(UserType.getUserTypeByName(rs.getString("user_type").toUpperCase())));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM appuser";
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(new User()
                    .setId(rs.getLong("id"))
                    .setDateOfBirth(convertDateToLocalDate(rs.getDate("birthday")))
                    .setEmail(rs.getString("email"))
                    .setGender(Gender.valueOf(rs.getInt("gender")))
                    .setUserType(UserType.getUserTypeByName(rs.getString("user_type").toUpperCase())));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Optional<User> updateUser(User user) {
        String sql = "UPDATE appuser SET birthday=?, email=?, gender=?, user_type=? WHERE id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, convertLocalDateToDate(user.getDateOfBirth()));
            ps.setString(2, user.getEmail());
            ps.setInt(3, user.getGender().getCode());
            ps.setString(4, user.getUserType().getName().toUpperCase());
            ps.setLong(5, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(user);
    }

    @Override
    public boolean deleteUserById(Long id) {
        String sql = "DELETE FROM appuser WHERE id=?";
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
        String sql = "INSERT INTO appuser(birthday, email, gender, user_type) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, convertLocalDateToDate(user.getDateOfBirth()));
            ps.setString(2, user.getEmail());
            ps.setInt(3, user.getGender().getCode());
            ps.setString(4, user.getUserType().getName().toUpperCase());
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
}
