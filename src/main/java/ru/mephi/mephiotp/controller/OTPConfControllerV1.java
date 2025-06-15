package ru.mephi.mephiotp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mephi.mephiotp.dto.OTPConfDTO;
import ru.mephi.mephiotp.service.OTPConfService;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/otpconf")
@RequiredArgsConstructor
public class OTPConfControllerV1 {
    private final OTPConfService otpConfService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OTPConfDTO> getOTP() {
        return ResponseEntity.ok(otpConfService.getOTP());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OTPConfDTO> updateOTP(@RequestBody OTPConfDTO otp) {
        return ResponseEntity.ok(otpConfService.updateOTP(otp));
    }
}
