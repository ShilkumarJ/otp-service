package com.example.otpservice.repository;

import com.example.otpservice.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    void shouldSaveUser() {
        User user = new User(null, "abc@gmail.com", "+913434343", null, null);

        User savedUser = userRepository.save(user);

        Optional<User> actualUser = userRepository.findById(savedUser.getId());

        assertThat(actualUser).isPresent();
        assertThat(actualUser.get().getEmailAddress()).isEqualTo(user.getEmailAddress());
        assertThat(actualUser.get().getPhoneNumber()).isEqualTo(user.getPhoneNumber());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}
