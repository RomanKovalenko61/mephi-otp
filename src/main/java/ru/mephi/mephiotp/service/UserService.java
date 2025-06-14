package ru.mephi.mephiotp.service;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mephi.mephiotp.dto.UserDto;
import ru.mephi.mephiotp.exception.ResourceNotFoundException;
import ru.mephi.mephiotp.model.User;
import ru.mephi.mephiotp.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public UserDto createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResourceNotFoundException("Email already exists: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return toDto(savedUser);
    }

    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public UserDto updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    if (userDetails.getUsername() != null) {
                        user.setUsername(userDetails.getUsername());
                    }
                    if (userDetails.getPassword() != null) {
                        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
                    }
                    if (userDetails.getRole() != null) {
                        user.setRole(userDetails.getRole());
                    }
                    if (userDetails.getEmail() != null) {
                        user.setEmail(userDetails.getEmail());
                    }
                    User updatedUser = userRepository.save(user);
                    return toDto(updatedUser);
                })
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public UserDto deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        var user = getUserById(id);
        userRepository.deleteById(id);
        return user;
    }

    public User findByEmail(@NotBlank String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getRole().toString(), user.getEmail());
    }
}
