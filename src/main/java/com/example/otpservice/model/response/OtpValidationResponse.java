package com.example.otpservice.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class OtpValidationResponse {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty
    private String message;
}
