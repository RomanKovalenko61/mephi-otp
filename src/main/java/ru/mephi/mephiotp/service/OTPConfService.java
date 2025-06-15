package ru.mephi.mephiotp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mephi.mephiotp.dto.OTPConfDTO;
import ru.mephi.mephiotp.exception.ResourceNotFoundException;
import ru.mephi.mephiotp.model.OTPConfiguration;
import ru.mephi.mephiotp.repository.OTPConfigurationRepository;

@Service
@RequiredArgsConstructor
public class OTPConfService {
    private final OTPConfigurationRepository repository;

    public OTPConfDTO getOTP() {
        return repository.findById(1L).map(this::toDto).orElseThrow(() -> new ResourceNotFoundException("OTP конфигурация не найдена"));
    }

    public OTPConfDTO updateOTP(OTPConfDTO otp) {
        return repository.findById(1L)
                .map(existingOtp -> {
                    existingOtp.setLength(otp.getLength());
                    existingOtp.setAliveTime(otp.getAliveTime());
                    OTPConfiguration updatedOtp = repository.save(existingOtp);
                    return toDto(updatedOtp);
                })
                .orElseThrow(() -> new ResourceNotFoundException("OTP конфигурация не найдена"));
    }

    private OTPConfDTO toDto(OTPConfiguration otp) {
        return new OTPConfDTO(
                otp.getLength(),
                otp.getAliveTime()
        );
    }
}
