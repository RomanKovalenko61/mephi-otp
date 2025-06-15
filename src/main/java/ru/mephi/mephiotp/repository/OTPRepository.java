package ru.mephi.mephiotp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mephi.mephiotp.model.OTP;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {
}
