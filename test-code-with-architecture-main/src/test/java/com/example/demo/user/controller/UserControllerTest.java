package com.example.demo.user.controller;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.port.UserReadService;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class UserControllerTest {

    @Test
    void 사용자는_특정_유저의_정보를_전달_받을_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder().build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("test@test.com")
                .nickname("test")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .build());

        // when
        ResponseEntity<UserResponse> result = UserController.builder()
                .userReadService(testContainer.userReadService) // public으로 선언이 되어있기 때문에 이런식으로 외부에서 직접 접근 가능
                .build()
                .getUserById(1);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getEmail()).isEqualTo("test@test.com");
        assertThat(result.getBody().getNickname()).isEqualTo("test");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

//    @Test
//    void 사용자는_존재하지_않는_유저의_아이디로_api를_호출할_경우_404_응답을_받는다() throws Exception {
//        // given
//        UserController userController = UserController.builder()
//                .userReadService(new UserReadService() {
//                    @Override
//                    public User getByEmail(String email) {
//                        return null;
//                    }
//
//                    @Override
//                    public User getById(long id) {
//                        throw  new ResourceNotFoundException("Users", id);
//                    }
//                })
//                .build();
//
//        // when
//        // then
//        assertThatThrownBy(() -> {
//            userController.getUserById(123456789);
//        }).isInstanceOf(ResourceNotFoundException.class);
//    }
//
//    @Test
//    void 사용자는_인증_코드로_계정응ㄹ_활성화_시킬_수_있다() throws Exception {
//        // given
//        // when
//        // then
//        mockMvc.perform(get("/api/users/2/verify")
//                    .queryParam("certificationCode", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
//                .andExpect(status().isFound());
//    }
//
//    @Test
//    void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() throws Exception {
//        // given
//        // when
//        // then
//        mockMvc.perform(get("/api/users/me")
//                        .header("EMAIL", "ddd8177@naver.com"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.address").value("Seoul"))
//                .andExpect(jsonPath("$.nickname").value("ddd8177"));
//    }
//
//    @Test
//    void 사용자는_내_정보를_수정할_수_있다() throws Exception {
//        // given
//        UserUpdate userUpdate = UserUpdate.builder()
//                .nickname("ddd81772")
//                .address("Seoul1")
//                .build();
//
//        // when
//        // then
//        mockMvc.perform(get("/api/users/me")
//                        .header("EMAIL", "ddd8177@naver.com")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(userUpdate)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.address").value("Seoul1"))
//                .andExpect(jsonPath("$.nickname").value("ddd81772"));
//    }

}