package org.zerock.guestbook.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Guestbook extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gno;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 1500, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content){
        this.content = content;
    }
}

//    create table guestbook (
//        gno bigint not null auto_increment,
//        moddate datetime(6),
//        regdate datetime(6),
//        content varchar(1500) not null,
//        title varchar(100) not null,
//        writer varchar(50) not null,
//        primary key (gno)
//        ) engine=InnoDB
