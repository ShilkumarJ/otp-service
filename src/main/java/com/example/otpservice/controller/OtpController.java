package com.example.otpservice.controller;

import com.example.otpservice.entity.User;
import com.example.otpservice.model.ErrorResponse;
import com.example.otpservice.model.OtpNotFound;
import com.example.otpservice.model.request.OtpRequest;
import com.example.otpservice.model.request.OtpValidationRequest;
import com.example.otpservice.model.response.OtpResponse;
import com.example.otpservice.model.response.OtpValidationResponse;
import com.example.otpservice.service.OtpService;
import com.example.otpservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class OtpController {

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    public OtpController(UserService userService, OtpService otpService) {
        this.userService = userService;
        this.otpService = otpService;
    }

    @PostMapping("/otp")
    public ResponseEntity<OtpResponse> generateOtp(@RequestBody OtpRequest otpRequest){
        User user = userService.findByPhoneNumber(otpRequest.getPhoneNumber());
        if(Objects.isNull(user)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(otpService.generateOtp(user));
    }

    @PostMapping("/otp/validate")
    public ResponseEntity<OtpValidationResponse> validateOtp(@RequestBody OtpValidationRequest otpRequest){
        User user = userService.findById(otpRequest.getUserId());
        if(Objects.isNull(user)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(otpService.validateOtp(user, otpRequest));
    }

    @ExceptionHandler(OtpNotFound.class)
    public ResponseEntity<ErrorResponse> handleException(OtpNotFound otpNotFound){
        return ResponseEntity.badRequest().body(new ErrorResponse("otp_not_found", otpNotFound.getMessage()));
    }
}
