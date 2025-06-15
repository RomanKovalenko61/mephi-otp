package ru.mephi.mephiotp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OTPGenerateRequest {
    @NotBlank(message = "userId cannot be blank")
    private Long userId;
    @NotBlank(message = "operationId cannot be blank")
    private Long operationId;
}
