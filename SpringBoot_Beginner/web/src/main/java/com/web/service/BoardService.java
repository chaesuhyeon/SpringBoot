package com.web.service;

import com.web.domain.Board;
import com.web.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

//    @Autowired // 생성자 1개면 생략 가능 -> 생성자까지 생략할꺼면 @RequiredArgsConstructor 사용
    public BoardService(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    // pageable로 넘어온 pageNumber 객체가 0 이하일 때 0으로 초기화. 기본페이지 크기인 10으로 새로운 PageRequest 객체를 만들어서 페이징 처리된 게시글 리스트 반환
    public Page<Board> findBoardList(Pageable pageable){
        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        return boardRepository.findAll(pageable);
    }

    public Board findBoardByIdx(Long idx){
        return boardRepository.findById(idx).orElse(new Board());
        //orElse() ->  Optional 클래스 객체가 가지고 있는 실제 값이 null일 경우 무슨 값으로 대체해서 return 해줘야할지를 정의
    }

}
