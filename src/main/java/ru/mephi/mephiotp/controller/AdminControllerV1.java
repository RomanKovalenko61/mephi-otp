package ru.mephi.mephiotp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mephi.mephiotp.dto.UserDto;
import ru.mephi.mephiotp.model.User;
import ru.mephi.mephiotp.service.UserService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminControllerV1 {
    @Autowired
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsersWithoutAdmins() {
        List<UserDto> users = userService.getAllUsersWithoutAdmins();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        var userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        var userDto = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable Long id) {
        var userDto = userService.deleteUser(id);
        return ResponseEntity.ok(userDto);
    }
}
