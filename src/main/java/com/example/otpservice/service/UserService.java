package com.example.otpservice.service;

import com.example.otpservice.entity.User;
import com.example.otpservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).get();
    }
}
