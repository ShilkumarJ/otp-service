package com.example.otpservice.repository;

import com.example.otpservice.entity.Otp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OtpRepositoryTest {

    @Autowired
    private OtpRepository otpRepository;

    @Test
    void shouldSaveOtp() {
        String otpValue = "1234";
        OffsetDateTime expirationTime = OffsetDateTime.now().plus(2, ChronoUnit.MINUTES);
        Otp otp = new Otp(null, 1L, otpValue, expirationTime, null, null);

        otpRepository.save(otp);

        Optional<Otp> actualOtp = otpRepository.findById(1L);

        assertThat(actualOtp).isPresent();
        assertThat(actualOtp.get().getId()).isNotNull();
        assertThat(actualOtp.get().getOtp()).isEqualTo(otpValue);
//        assertThat(actualOtp.get().getExpirationTime().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
//                .isEqualTo(expirationTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        assertThat(actualOtp.get().getCreatedAt()).isNotNull();
        assertThat(actualOtp.get().getUpdatedAt()).isNotNull();
    }

    @AfterEach
    void tearDown() {
        otpRepository.deleteAll();
    }
}
