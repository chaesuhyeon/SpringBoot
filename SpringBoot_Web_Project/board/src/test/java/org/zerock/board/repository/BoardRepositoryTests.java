package org.zerock.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

import javax.xml.validation.Validator;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void insertBoard(){

        IntStream.rangeClosed(1,100).forEach(i->{
            Member member = Member.builder().email("user"+i+"@aaa.com").build();

            Board board = Board.builder()
                    .title("Title..." + i)
                    .content("Content...."+i)
                    .writer(member)
                    .build();
            boardRepository.save(board);
        });
    }

    @Transactional // @ManyToOne fetch방식을 lazy로 지정해줬으므로 사용 (안쓰면 board테이블만 가져오고 끝나므로 member 테이블에 있는 getWriter를 가져오지 못해서 에러발생)
    @Test
    public void testRead1(){
        Optional<Board> result = boardRepository.findById(100L);// 데이터베이스에 존재하는 번호

        Board board = result.get();

        System.out.println(result); // Optional[Board(bno=100, title=Title...100, content=Content....100)]
        System.out.println(board); // Board(bno=100, title=Title...100, content=Content....100)
        System.out.println(board.getWriter()); // Member(email=user100@aaa.com, password=1111, name=USER100)
    }
}
