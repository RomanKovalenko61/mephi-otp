package ru.mephi.mephiotp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.mephi.mephiotp.model.Role;

@Data
public class SignUpRequest {
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank
    private Role role;
}
