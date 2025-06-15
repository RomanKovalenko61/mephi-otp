package ru.mephi.mephiotp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.mephi.mephiotp.dto.JwtAuthenticationResponse;
import ru.mephi.mephiotp.dto.SignInRequest;
import ru.mephi.mephiotp.dto.SignUpRequest;
import ru.mephi.mephiotp.dto.UserDto;
import ru.mephi.mephiotp.exception.AdminAlreadyExistsException;
import ru.mephi.mephiotp.exception.RegisterEmptyFieldException;
import ru.mephi.mephiotp.exception.ResourceNotFoundException;
import ru.mephi.mephiotp.model.User;
import ru.mephi.mephiotp.service.JwtService;
import ru.mephi.mephiotp.service.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthControllerV1 {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignUpRequest request) {
        if (request.getRole().toString().equals("ADMIN")) {
            boolean exists = userService.getAllAdmins().stream()
                    .anyMatch(user -> user.getRole().equals("ADMIN"));
            if (exists) {
                throw new AdminAlreadyExistsException("Admin already exists");
            }
        }

        if (request.getUsername() == null || request.getPassword() == null || request.getEmail() == null || request.getRole() == null) {
            throw new RegisterEmptyFieldException("Все поля обязательны для заполнения (username, password, email, role)");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .role(request.getRole())
                .build();

        var userDto = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody SignInRequest request) {
        User user = userService.findByEmail(request.getEmail());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("Invalid email or password");
        }
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }
}