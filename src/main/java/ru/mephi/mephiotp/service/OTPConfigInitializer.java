package ru.mephi.mephiotp.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.mephi.mephiotp.model.OTPConfiguration;
import ru.mephi.mephiotp.repository.OTPConfigurationRepository;

@Service
@RequiredArgsConstructor
public class OTPConfigInitializer {
    private final OTPConfigurationRepository repository;

    @Value("${otp.length:6}")
    private int length;

    @Value("${otp.aliveTime:300}")
    private int aliveTime;

    @PostConstruct
    @Transactional
    public void init() {
        OTPConfiguration config = repository.findById(1L).orElse(null);

        if (config == null) {
            var defaultConfig = new OTPConfiguration();
            defaultConfig.setId(1L);
            defaultConfig.setLength(length);
            defaultConfig.setAliveTime(aliveTime);
            repository.save(defaultConfig);
        }
    }
}
