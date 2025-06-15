package ru.mephi.mephiotp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mephi.mephiotp.model.OTP;

import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {
    Optional<OTP> findByOtpCodeAndUserIdAndOperationId(String otpCode, Long userId, Long operationId);
}
