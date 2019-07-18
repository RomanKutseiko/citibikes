package com.kutseiko.bicycle.repository.impl;

import com.kutseiko.bicycle.core.type.Gender;
import com.kutseiko.bicycle.core.type.UserType;
import com.kutseiko.bicycle.entity.User;
import com.kutseiko.bicycle.repository.UserRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserJdbcRepository implements UserRepository {

    private final Connection connection;

    @Override
    public Optional<User> getUserById(Long id) {
        String getStationById = "SELECT * FROM appuser WHERE id=?";
        User user = new User();
        Optional<User> result = Optional.empty();
        try {
            PreparedStatement ps = connection.prepareStatement(getStationById);
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
        String getUsers = "SELECT * FROM appuser";
        List<User> users = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(getUsers);
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
        String updateUserById = "UPDATE appuser SET birthday=?, email=?, gender=?, user_type=? WHERE id=?";
        try {
            PreparedStatement ps = connection.prepareStatement(updateUserById);
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
        String deleteUser = "DELETE FROM appuser WHERE id=?";
        boolean deleted = false;
        try {
            PreparedStatement ps = connection.prepareStatement(deleteUser);
            ps.setLong(1, id);
            deleted = ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deleted;
    }

    @Override
    public Optional<User> addUser(User user) {
        String insertUser = "INSERT INTO appuser(birthday, email, gender, user_type) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS);
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

    private LocalDate convertDateToLocalDate(java.sql.Date date) {
        return date.toLocalDate();
    }

    private java.sql.Date convertLocalDateToDate(LocalDate localDate) {
        return java.sql.Date.valueOf(localDate);
    }
}
