package com.example.demo.user.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


public class UserCreateControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 사용자는_회원_가입을_할_수있고_회원가입된_사용자는_PENDING_상태이다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
                .build();
        UserCreate userCreate = UserCreate.builder()
                .email("test@test.com")
                .nickname("test")
                .address("Seoul")
                .build();

        // when
        ResponseEntity<UserResponse> result = testContainer.userCreateController.create(userCreate);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("test@test.com");
        assertThat(result.getBody().getNickname()).isEqualTo("test");
        assertThat(result.getBody().getLastLoginAt()).isNull();
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(testContainer.userRepository.getById(1L).getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
    }
}
