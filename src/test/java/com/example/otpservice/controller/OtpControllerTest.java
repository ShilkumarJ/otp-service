package com.example.otpservice.controller;

import com.example.otpservice.entity.Otp;
import com.example.otpservice.entity.User;
import com.example.otpservice.model.request.OtpRequest;
import com.example.otpservice.model.request.OtpValidationRequest;
import com.example.otpservice.repository.OtpRepository;
import com.example.otpservice.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class OtpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;


    @Test
    void shouldReturnOtpForUser() throws Exception {
        String phoneNumber = "+913434343";
        User user = new User(null, "abc@gmail.com", phoneNumber, null, null);
        User savedUser = userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getOtpRequestString(phoneNumber)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user_id").value(savedUser.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.otp").isString());
    }

    @Test
    void shouldReturnUnauthorizedIfUserIsNotPresentInSystem() throws Exception {
        String phoneNumber = "+913434343";
        User user = new User(null, "abc@gmail.com", phoneNumber, null, null);
        User savedUser = userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getOtpRequestString("+913434355")))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void shouldValidateOtpForUser() throws Exception {
        String phoneNumber = "+913434343";
        User user = new User(null, "abc@gmail.com", phoneNumber, null, null);
        User savedUser = userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getOtpRequestString(phoneNumber)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user_id").value(savedUser.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.otp").isString());

        Otp otp = otpRepository.findByUserId(savedUser.getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/otp/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getOtpValidationRequestString(savedUser.getId(), otp.getOtp())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user_id").value(savedUser.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Otp validate successfully."));
    }

    @Test
    void shouldReturnErrorIfOtpIsNotValid() throws Exception {
        String phoneNumber = "+913434343";
        User user = new User(null, "abc@gmail.com", phoneNumber, null, null);
        User savedUser = userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getOtpRequestString(phoneNumber)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user_id").value(savedUser.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.otp").isString());

        Otp otp = otpRepository.findByUserId(savedUser.getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/otp/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getOtpValidationRequestString(savedUser.getId(), "random")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("otp_not_found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_description").value("Otp not found."));
    }

    private String getOtpRequestString(String phoneNumber) throws JsonProcessingException {
        OtpRequest otpRequest = new OtpRequest(phoneNumber);
        return objectMapper.writeValueAsString(otpRequest);
    }

    private String getOtpValidationRequestString(Long userId, String otp) throws JsonProcessingException {
        OtpValidationRequest otpRequest = new OtpValidationRequest(userId, otp);
        return objectMapper.writeValueAsString(otpRequest);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        otpRepository.deleteAll();
    }
}
