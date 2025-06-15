package ru.mephi.mephiotp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mephi.mephiotp.dto.OTPDto;
import ru.mephi.mephiotp.exception.ResourceNotFoundException;
import ru.mephi.mephiotp.model.OTP;
import ru.mephi.mephiotp.model.OTPCheck;
import ru.mephi.mephiotp.model.OTPStatus;
import ru.mephi.mephiotp.repository.OTPConfigurationRepository;
import ru.mephi.mephiotp.repository.OTPRepository;
import ru.mephi.mephiotp.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OTPService {
    private final OTPRepository OTPrepository;
    private final UserRepository userRepository;
    private final OTPConfigurationRepository OTPConfigurationRepository;
    private final NotificationService notificationService;

    @Transactional
    public OTPDto create(Long userId, Long operationId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        var otpConfig = OTPConfigurationRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("OTP Configuration not found"));

        String otp = generateOTP(otpConfig.getLength());
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = createdAt.plusSeconds(otpConfig.getAliveTime());
        var otpEntity = OTP.builder()
                .otpCode(otp)
                .operationId(operationId)
                .createdAt(createdAt)
                .expiresAt(expiresAt)
                .status(OTPStatus.ACTIVE)
                .user(user)
                .build();
        OTPrepository.save(otpEntity);

        notificationService.sendCodeToEmail(user, otp);
        notificationService.saveCodeToFile(user, otp);
        notificationService.sendCodeToSms(user, otp);
        notificationService.sendMessageToTelegram(otp);
        return otpDto(otpEntity);
    }

    @Transactional
    public OTPCheck validate(Long userId, String otpCode, Long operationId) {
        var otpEntity = OTPrepository.findByOtpCodeAndUserIdAndOperationId(otpCode, userId, operationId)
                .orElseThrow(() -> new ResourceNotFoundException("OTP not found for the given parameters"));

        if (otpEntity.getStatus() != OTPStatus.ACTIVE) {
            return OTPCheck.NOT_ACTIVE;
        }
        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            otpEntity.setStatus(OTPStatus.EXPIRED);
            return OTPCheck.EXPIRED;
        }
        otpEntity.setStatus(OTPStatus.USED);
        OTPrepository.save(otpEntity);
        return OTPCheck.REDEEMED;
    }

    public boolean delete(Long id) {
        var otpEntity = OTPrepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OTP not found with id: " + id));
        OTPrepository.delete(otpEntity);
        return true;
    }

    public OTPDto getById(Long id) {
        var otpEntity = OTPrepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OTP not found with id: " + id));
        return otpDto(otpEntity);
    }

    private String generateOTP(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }

    private OTPDto otpDto(OTP otpEntity) {
        return OTPDto.builder()
                .id(otpEntity.getId())
                .otpCode(otpEntity.getOtpCode())
                .operationId(otpEntity.getOperationId())
                .createdAt(otpEntity.getCreatedAt())
                .expiresAt(otpEntity.getExpiresAt())
                .status(otpEntity.getStatus())
                .userId(otpEntity.getUser().getId())
                .build();
    }
}
