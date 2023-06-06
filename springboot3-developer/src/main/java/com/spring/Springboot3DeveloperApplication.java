package com.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // created_at, updated_at 자동 업데이트
@SpringBootApplication
public class Springboot3DeveloperApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot3DeveloperApplication.class, args);
    }

}
