package com.example.otpservice.service;

import com.example.otpservice.entity.Otp;
import com.example.otpservice.entity.User;
import com.example.otpservice.model.OtpNotFound;
import com.example.otpservice.model.request.OtpValidationRequest;
import com.example.otpservice.model.response.OtpResponse;
import com.example.otpservice.model.response.OtpValidationResponse;
import com.example.otpservice.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    public OtpResponse generateOtp(User user) {
        int otpValue = ThreadLocalRandom.current().nextInt(100000, 1000000);
        OffsetDateTime expirationTime = OffsetDateTime.now().plus(2, ChronoUnit.MINUTES);
        otpRepository.save(new Otp(null, user.getId(), String.valueOf(otpValue), expirationTime, null, null));
        return new OtpResponse(user.getId(), String.valueOf(otpValue));
    }

    public OtpValidationResponse validateOtp(User user, OtpValidationRequest otpRequest) {
        Otp otp = otpRepository.findByUserId(user.getId());
        if (otp.getOtp().equals(otpRequest.getOtp())) {
            return new OtpValidationResponse(user.getId(), "Otp validate successfully.");
        }
        throw new OtpNotFound("Otp not found.");
    }
}
