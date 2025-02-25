package com.fourseason.delivery;

import com.fourseason.delivery.global.auth.dto.request.SignInRequestDto;
import com.fourseason.delivery.global.auth.dto.request.SignUpRequestDto;
import com.fourseason.delivery.global.exception.ErrorResponseEntity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthTest {

    private static final Logger log = LoggerFactory.getLogger(AuthTest.class);

    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.3");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        container.start();
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @Autowired
    TestRestTemplate restTemplate;

    SignUpRequestDto alice, bob;

    @BeforeEach
    void beforeEach() {
        alice = new SignUpRequestDto(
                "alice",
                "alice@mail.com",
                "alice123#",
                "010-1234-5678",
                null
        );
        bob = new SignUpRequestDto(
                "bob123",
                "bob123@mail.com",
                "bob12345^",
                "010-9876-5432",
                null
        );
    }

    @Test
    @DisplayName("회원 가입 성공 시 201 created")
    void createMember() {
        //  when
        ResponseEntity<Void> responseEntity = restTemplate
                .postForEntity("/api/sign-up", alice, Void.class);

        //  then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders().getLocation().toString()).isEqualTo("/api/sign-in");

    }

    @Test
    @DisplayName("로그인 성공 시 JWT 발행")
    void jwtValidTest() {
        //  given
        restTemplate
                .postForEntity("/api/sign-up", bob, Void.class);

        //  when
        SignInRequestDto request = new SignInRequestDto("bob123", "bob12345^");
        ResponseEntity<Void> responseEntity = restTemplate
                .postForEntity("/api/sign-in", request, Void.class);

        //  then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(responseEntity.getHeaders().get("Authorization")).get(0)).contains("Bearer ");
    }

    @Test
    @DisplayName("로그인 실패 시 400 에러 발생")
    void jwtInvalidTest() {
        SignInRequestDto request = new SignInRequestDto("alice", "alice123");

        ResponseEntity<ErrorResponseEntity> responseEntity = restTemplate
                .postForEntity("/api/sign-in", request, ErrorResponseEntity.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        ErrorResponseEntity errorResponseEntity = responseEntity.getBody();
        assertThat(errorResponseEntity.status()).isEqualTo(401);
        assertThat(errorResponseEntity.message()).isEqualTo("ID / 비밀번호가 잘못되었습니다.");
    }

    @AfterAll
    static void tearDown() {
        container.stop();
    }
}