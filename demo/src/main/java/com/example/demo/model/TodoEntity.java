package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Todo") // 테이블 이름 지정
public class TodoEntity {
    
    @Id // 기본키가 될 필드에 지정
    @GeneratedValue(generator = "system-uuid") // id를 자동생성하겠다.
    @GenericGenerator(name = "system-uuid" , strategy = "uuid") // 기본 Generator가 아닌 나만의 Generator를 사용하고 싶을 때 사용
    private String id; // 이 오브젝트의 아이디
    private String userId; // 이 오브젝트를 생성한 유저의 아이디
    private String title;
    private boolean done; // todo를 완료한 경우(checked)
}
