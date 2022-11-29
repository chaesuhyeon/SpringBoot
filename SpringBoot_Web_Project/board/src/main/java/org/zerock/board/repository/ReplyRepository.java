package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    //Board 삭제시에 댓글들 삭제
    @Modifying // JPQL을 이용해서 update, delete를 실행하기 위해서는 @Modifying어노테이션을 같이 추가해야 함
    @Query("delete from Reply r where r.board.bno =:bno")
    void deleteByBno(@Param("bno") Long bno);

    //게시물로 댓글 목록 가져오기
    List<Reply> getRepliesByBoardOrderByRno(Board board);
}
