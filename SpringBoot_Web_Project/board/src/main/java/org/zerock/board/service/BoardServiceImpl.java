package org.zerock.board.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.ReplyRepository;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{

    private final BoardRepository repository; // 자동주입 final

    private final ReplyRepository replyRepository;
    @Override
    public Long register(BoardDTO dto) {

        log.info("dto={}" , dto);

        Board board = dtoToEntity(dto);

        repository.save(board);

        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO){

        log.info("pageRequestDTO={}", pageRequestDTO); // pageRequestDTO=PageRequestDTO(page=1, size=10, type=null, keyword=null)

        Function<Object[] , BoardDTO> fn = (en -> entityToDTo((Board)en[0], (Member)en[1] , (Long)en[2]));

        Page<Object[]> result = repository.getBoardWithReplyCount(
                pageRequestDTO.getPageable(Sort.by("bno").descending()));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BoardDTO get(Long bno){

        Object result = repository.getBoardByBno(bno);
        Object[] arr = (Object[]) result;

        log.info("arr={}" , arr);

        return entityToDTo((Board) arr[0] , (Member) arr[1] , (Long) arr[2]);
    }

    @Transactional
    @Override
    public void removeWithReplies(Long bno){ // 삭제기능 구현. 트랜잭션 추가  -> 게시글에 있는 댓글먼저 삭제하고 게시글 삭제

        //댓글 부터 삭제
        replyRepository.deleteByBno(bno);

        repository.deleteById(bno);
    }

    @Transactional // 조회와 수정을 같이 하기 때문에 필요
    @Override
    public void modify(BoardDTO boardDTO) {

        Board board = repository.getReferenceById(boardDTO.getBno()); // getOne deprecated돼서 getReferenceById 사용

        board.changeTitle(boardDTO.getTitle());
        board.changeContent(boardDTO.getContent());

        repository.save(board);
    }

}
