package ru.mephi.mephiotp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.mephiotp.model.OTPStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OTPDto {
    private Long id;
    private String otpCode;
    private Long operationId;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private OTPStatus status;
    private Long userId;
}
