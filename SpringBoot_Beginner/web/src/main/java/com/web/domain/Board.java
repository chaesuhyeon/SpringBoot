package com.web.domain;

import com.web.domain.enums.BoardType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table
public class Board implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키가 자동으로 할당되도록 설정 , 키 생성을 데이터베이스에 위임하는 IDENTITY전략 사용
    private Long idx;

    @Column
    private String title;

    @Column
    private String subTitle;

    @Column
    private String content;

    @Column
    @Enumerated(EnumType.STRING) // Enum 타입 매핑용 어노테이션 , 실제로는 자바 enum형이지만 데이터베이스의 String형으로 변환하여 저장하겠다고 선언
    private BoardType boardType;

    @Column
    private LocalDateTime createdDate;

    @Column
    private LocalDateTime updatedDate;

    @OneToOne(fetch = FetchType.LAZY) // 도메인 Board와 Board가 필드값으로 갖고 있는 도메인 User를 1:1관계로 설정 , 실제로 DB에 저장될 때는 User객체가 저장되는 것이 아니라 PK인 user.idx값이 저장
    private User user;

    @Builder
    public Board(String title, String subTitle, String content, BoardType boardType, LocalDateTime createdDate, LocalDateTime updatedDate, User user){
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.boardType = boardType;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.user = user;
    }

}
