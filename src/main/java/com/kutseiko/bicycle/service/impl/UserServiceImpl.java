package com.kutseiko.bicycle.service.impl;

import com.kutseiko.bicycle.DTO.UserDto;
import com.kutseiko.bicycle.core.type.Gender;
import com.kutseiko.bicycle.core.type.UserType;
import com.kutseiko.bicycle.entity.User;
import com.kutseiko.bicycle.repository.UserRepository;
import com.kutseiko.bicycle.service.UserService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public Optional<User> createUser(UserDto userDto) {
        User user = new User().setEmail(userDto.getEmail()).setDateOfBirth(userDto.getDateOfBirth())
            .setGender(Gender.valueOf(userDto.getGender())).setUserType(UserType.getUserTypeByName(userDto.getUserType()));
        return userRepository.addUser(user);
    }

    @Override
    public boolean deleteUserById(Long id) {
        return userRepository.deleteUserById(id);
    }

    @Override
    public Optional<User> updateUser(Long id, UserDto userDto) {
        User user = new User().setId(id).setEmail(userDto.getEmail()).setDateOfBirth(userDto.getDateOfBirth())
            .setGender(Gender.valueOf(userDto.getGender())).setUserType(UserType.getUserTypeByName(userDto.getUserType()));
        return userRepository.updateUser(user);
    }
}
