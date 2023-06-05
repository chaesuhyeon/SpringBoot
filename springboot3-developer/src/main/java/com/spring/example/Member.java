package com.spring.example;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access= AccessLevel.PROTECTED) // 기본 생성자 , 접근 제어자는 public 또는 protected여야 하는데, public보다 protected가 더 안전함
@AllArgsConstructor
@Getter
@Entity
public class Member {
    @Id // id필드를 기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 자동으로 1씩 증가
    @Column(name = "id", updatable = false)
    private Long id; // DB 테이블의 id 컬럼과 매칭

    @Column(name = "name" , nullable = false) // name이라는 not null 컬럼과 매핑
    private String name; // DB 테이블의 name 컬럼과 매칭
}