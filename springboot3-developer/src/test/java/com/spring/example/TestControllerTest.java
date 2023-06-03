package com.spring.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 테스트용 애플리케이션 컨텍스트 생성, @SpringBootApplication이 있는 클래스를 찾고 그 클래스에 포함되어 있는 빈을 찾은 다음 테스트용 애플리케이션 컨텍스트라는 것을 만듦
@AutoConfigureMockMvc // MockMvc 생성, MockMvc는 어플리케이션을 서버에 배포하지 않고도 테스트용 MVC 환경을 만들어 요청 및 전송, 응답 기능을 제공하는 유틸리티 클래스
class TestControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach // 테스트 실행 전 실행하는 메서드
    public void mockMvcSetUp(){
        this.mockMvc = MockMvcBuilders
                                .webAppContextSetup(context)
                                .build();
    }

    @AfterEach // 테스트 실행 후 실행하는 메서드
    public void cleanUp() {
        memberRepository.deleteAll();
    }

    @DisplayName("getAllMembers: 아티클 조회에 성공한다.")
    @Test
    public void getAllMembers() throws Exception {
        //given (멤버를 저장)
        final String url = "/test";
        Member savedMember = memberRepository.save(new Member(1L, "홍길동"));

        //when (멤버 리스트를 조회하는 API 호출)
        final ResultActions result = mockMvc.perform(get(url) // perform : 요청을 전송하는 역할을 하는 메서드
                .accept(MediaType.APPLICATION_JSON)); // accept : 요청을 보낼 때 무슨 타입으로 응답을 받을지 결정하는 메서드

        //then (응답 코드가 200이고 반환받은 값 중에서 0번째 요소의 id와 name이 저장한 값과 같은지 확인)
        result
                .andExpect(status().isOk())
                //응답의 0번째 값이 DB에서 저장한 값과 같은지 확인
                .andExpect(jsonPath("$[0].id").value(savedMember.getId())) // jsonPath("$[0].필드명") 은 JSON 응답값의 값을 가져오는 역할을 하는 메서드
                .andExpect(jsonPath("$[0].name").value(savedMember.getName()));
    }

}