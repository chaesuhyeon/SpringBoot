package com.example.redis.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data // 예제 실습의 편의를 위해 Data 사용 (실제 개발 할 때는 Entity에 @Data 지양)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String orderCode;
    private String orderObject;
    private String orderStatus;
    private Integer orderPrice;

}
