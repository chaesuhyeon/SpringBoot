package com.spring.toby.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JdbcRunner implements CommandLineRunner {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {

        // CUSTOMER 테이블이 존재하면 삭제
        jdbcTemplate.execute("DROP TABLE CUSTOMER IF EXISTS");

        // CUSTOMER 테이블 생성
        jdbcTemplate.execute("CREATE TABLE CUSTOMER (id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

        // CUSTOMER 테이블에 데이터 삽입
        List.of("Daniel Woo", "John Dean", "Tom Bloch", "Jane Long").forEach(
                name -> {
                    String[] names = name.split(" ");
                    jdbcTemplate.update("INSERT INTO CUSTOMER(first_name, last_name) VALUES (?, ?)", names[0], names[1]);
                }
        );

        // CUSTOMER 테이블의 모든 데이터를 조회
        jdbcTemplate.query("SELECT id, first_name, last_name FROM CUSTOMER",
                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name")))
                .forEach(customer -> System.out.println(customer.toString()));
    }
}
