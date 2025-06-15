package ru.mephi.mephiotp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.mephi.mephiotp.dto.OTPDto;
import ru.mephi.mephiotp.dto.OTPGenerateRequest;
import ru.mephi.mephiotp.dto.OTPValidateRequest;
import ru.mephi.mephiotp.service.OTPService;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/otp")
@RequiredArgsConstructor
public class OTPControllerV1 {
    private final OTPService otpService;

    @PostMapping("/generate")
    public ResponseEntity<OTPDto> generateOTP(@RequestBody OTPGenerateRequest request) {
        if (request.getUserId() == null || request.getOperationId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID and Operation ID must not be null");
        }
        var otpDto = otpService.create(request.getUserId(), request.getOperationId());
        return ResponseEntity.status(HttpStatus.CREATED).body(otpDto);
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateOTP(@RequestBody OTPValidateRequest request) {
        if (request.getUserId() == null || request.getOtp() == null || request.getOperationId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID, OTP, and Operation ID must not be null");
        }
        var status = otpService.validate(request.getUserId(), request.getOtp(), request.getOperationId());
        return ResponseEntity.ok(Map.of("status", status.name()));
    }

    @RequestMapping("/api/v1/admin")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOTP(@PathVariable Long id) {
        var result = otpService.delete(id);
        return ResponseEntity.ok(Map.of("deleted: ", result));
    }

    @RequestMapping("/api/v1/admin")
    @GetMapping("/{id}")
    public ResponseEntity<OTPDto> getOTPById(@PathVariable Long id) {
        var otpDto = otpService.getById(id);
        if (otpDto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "OTP not found with id: " + id);
        }
        return ResponseEntity.ok(otpDto);
    }
}
