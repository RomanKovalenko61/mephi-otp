package ru.mephi.mephiotp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mephi.mephiotp.model.OTPConfiguration;

@Repository
public interface OTPConfigurationRepository extends JpaRepository<OTPConfiguration, Long> {
}
