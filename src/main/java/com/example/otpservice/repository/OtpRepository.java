package com.example.otpservice.repository;

import com.example.otpservice.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<Otp, Long> {

    Otp findByUserId(Long userId);
}
