package com.example.demo.medium;

import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.service.PostServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/post-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class PostServiceTest {
    @Autowired
    private PostServiceImpl postService;

    @Test
    void getById_는_존재하는_게시물을_내려준다(){
        // given
        // when
        Post result = postService.getPostById(1);

        // then
        assertThat(result.getContent()).isEqualTo("helloword");
        assertThat(result.getWriter().getEmail()).isEqualTo("ddd8177@naver.com");
    }

    @Test
    void postCreateDto_를_이용하여_게시물을_생성할_수_있다() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("foobar")
                .build();

        // when
        Post result = postService.create(postCreate);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("foobar");
        assertThat(result.getCreatedAt()).isGreaterThan(0);
    }

    @Test
    void postCreateDto_를_이용하여_유저를_수정할_수_있다() {
        // given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("hello world :)")
                .build();


        // when
        Post result = postService.update(1, postUpdate);

        // then
        Post post = postService.getPostById(1);

        assertThat(post).isNotNull();
        assertThat(post.getContent()).isEqualTo("hello world :)");
        assertThat(result.getModifiedAt()).isGreaterThan(0);
    }
}