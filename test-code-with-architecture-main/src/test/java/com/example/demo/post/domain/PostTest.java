package com.example.demo.post.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    @Test
    public void PostCreate_게시물을_만들_수_있다() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("helloworld")
                .build();

        User writer = User.builder()
                .id(1L)
                .email("test@test.com")
                .nickname("tester")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();

        // when
        Post post = Post.from(writer, postCreate, new TestClockHolder(1678530673958L));

        // then
        assertThat(post.getContent()).isEqualTo("helloworld");
        assertThat(post.getCreatedAt()).isEqualTo(1678530673958L);
        assertThat(post.getWriter().getEmail()).isEqualTo("test@test.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("tester");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

}