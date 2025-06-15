package ru.mephi.mephiotp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String otpCode;

    @Column
    private Long operationId;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime expiresAt;

    @Column
    @Enumerated(EnumType.STRING)
    private OTPStatus status;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
}
