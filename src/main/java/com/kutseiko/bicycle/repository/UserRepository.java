package com.kutseiko.bicycle.repository;

import com.kutseiko.bicycle.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> getUserById(Long id);

    List<User> getAllUsers();

    Optional<User> updateUser(User user);

    boolean deleteUserById(Long id);

    Optional<User> addUser(User user);
}
