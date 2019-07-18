package com.kutseiko.bicycle.service;

import com.kutseiko.bicycle.DTO.UserDto;
import com.kutseiko.bicycle.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(Long id);

    List<User> getAllUsers();

    Optional<User> createUser(UserDto userDto);

    boolean deleteUserById(Long id);

    Optional<User> updateUser(Long id, UserDto userDto);
}
