package com.example.demo.user.domain;

import com.example.demo.mock.TestUuidHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    public void UserCreate_객체로_생성할_수_있다() {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("test@test.com")
                .nickname("test")
                .address("Seoul")
                .build();

        // when
        User user = User.from(userCreate, new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"));

        // then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("test@test.com");
        assertThat(user.getNickname()).isEqualTo("test");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");

    }

    @Test
    public void UserUpdate_객체로_데이터를_업데이트_할_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .nickname("test")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .build();


        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("test2")
                .address("Busan")
                .build();

        // when
        user = user.update(userUpdate);

        // then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getNickname()).isEqualTo("test2"); // 변경된 값
        assertThat(user.getAddress()).isEqualTo("Busan");// 변경된 값
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    public void 로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다() {
        // given
        // when
        // then
    }

    @Test
    public void 유효한_인증_코드로_계정을_활성화_할_수_있다() {
        // given
        // when
        // then
    }

    @Test
    public void 잘못된_인증_코드로_계정을_활성화_하려면_에러를_던진다() {
        // given
        // when
        // then
    }
}
