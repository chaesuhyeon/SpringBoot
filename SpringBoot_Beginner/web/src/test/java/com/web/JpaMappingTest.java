package com.web;

import com.web.domain.Board;
import com.web.domain.User;
import com.web.domain.enums.BoardType;
import com.web.repository.BoardRepository;
import com.web.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@DataJpaTest // JPA 테스트를 위한 어노테이션. 테스트가 끝날 때 마다 자동 롤백을 해줌(DB에 영향 x)
public class JpaMappingTest {
    private final String boardTestTitle = "테스트";
    private final String email = "test@gmail.com";
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    BoardRepository boardRepository;
    
    @Before // 각 테스트가 실행되기 전에 실행될 메서드 선언
    public void init(){
        User user = userRepository.save(User.builder()
                    .name("havi")
                    .password("test")
                    .email(email)
                    .createdDate(LocalDateTime.now())
                    .build());

        boardRepository.save(Board.builder()
                .title(boardTestTitle)
                .subTitle("서브 타이틀")
                .content("콘텐츠")
                .boardType(BoardType.free)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .user(user).build());
    }

    @Test // 실제 테스트가 진행될 메서드 선언
    public void 제대로_생성됐는지_테스트(){
        User user = userRepository.findByEmail(email); // init()에서 저장된 user를 email로 조회

        assertThat(user.getName() , is("havi"));
        assertThat(user.getPassword(), is("test"));
        assertThat(user.getEmail() , is("test@gmail.com"));

        Board board = boardRepository.findByUser(user);
        assertThat(board.getTitle(), is(boardTestTitle));
        assertThat(board.getSubTitle(), is("서브 타이틀"));
        assertThat(board.getContent(), is("콘텐츠"));
        assertThat(board.getBoardType(), is(BoardType.free));

    }


}
