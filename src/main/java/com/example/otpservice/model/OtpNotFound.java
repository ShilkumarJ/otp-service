package com.example.otpservice.model;

public class OtpNotFound extends RuntimeException {

    public OtpNotFound(String message) {
        super(message);
    }
}
