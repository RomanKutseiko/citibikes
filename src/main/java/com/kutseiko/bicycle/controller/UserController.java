package com.kutseiko.bicycle.controller;

import com.kutseiko.bicycle.DTO.UserDto;
import com.kutseiko.bicycle.entity.User;
import com.kutseiko.bicycle.service.UserService;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public Optional<User> getUserByID(@PathVariable(name = "id")Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public Optional<User> createUser(@RequestBody @Valid UserDto UserDto) {
        return userService.createUser(UserDto);
    }

    @PutMapping("/{id}")
    public Optional<User> updateUser(@RequestBody @Valid UserDto UserDto, @PathVariable(name = "id")Long id) {
        return userService.updateUser(id, UserDto);
    }

    @DeleteMapping("/{id}")
    public boolean updateUser(@PathVariable(name = "id")Long id) {
        return userService.deleteUserById(id);
    }

}
