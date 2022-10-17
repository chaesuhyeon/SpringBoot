package org.zerock.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {

    private int page;
    private int size;
    private String type;
    private String keyword;

    public PageRequestDTO(){
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(page - 1, size, sort); 
        // JPA를 이용하는 경우 페이지 번호가 0부터 시작한다는 점을 감안해서 1페이지의 경우 0이 될 수 있도록 page-1을 해줌
        // 정렬은 나중에 다양한 상황에 쓰기 위해서 별도의 파라미터로 받음
    }
}
