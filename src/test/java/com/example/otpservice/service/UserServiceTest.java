package com.example.otpservice.service;

import com.example.otpservice.entity.User;
import com.example.otpservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldFindUserForGivenPhoneNumber() {
        String phoneNumber = "+91874378";
        User expectedUser = new User(1L, "abc@gmail.com", phoneNumber, null, null);

        Mockito.when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(expectedUser);

        User actualUser = userService.findByPhoneNumber(phoneNumber);

        assertThat(actualUser)
                .isEqualTo(expectedUser);
    }
}
