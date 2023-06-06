package com.example.otpservice.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OtpValidationRequest {

    @JsonProperty("user_id")
    private Long userId;

    private String otp;

}
