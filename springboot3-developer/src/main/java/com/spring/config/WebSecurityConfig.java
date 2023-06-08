package com.spring.config;

import com.spring.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final UserDetailService userService;

    // 스프링 시큐리티 기능 비활성화
    // 인증, 인가를 모든 곳에 모두 적용하지 않는다. 일반적으로 정적 리소스(이미지, HTML 파일)에 설정한다.
    // 정적 리소스만 스프링 시큐리티 사용을 비활성화하는데 static 하위 경로에 있는 리소스와 h2 데이터를 확인하는데 사용하는  h2-console 하위 url을 대상으로
    // ignoring() 메서드를 사용한다.
    @Bean
    public WebSecurityCustomizer configure() {
        return (web -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/static/**"));
    }

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeRequests() // 인증, 인가 설정
                    .requestMatchers("/login", "/signup", "/user").permitAll()
                    .anyRequest().authenticated()
                .and()
                .formLogin() // 폼 기반 로그인 설정
                    .loginPage("/login") // 로그인 페이지 경로를 설정한다.
                    .defaultSuccessUrl("/articles") // 로그인이 완료되었을 때 이동할 경로를 설정한다.
                .and()
                .logout()// 로그아웃 설정
                    .logoutSuccessUrl("/login") // 로그아웃이 완료되었을 때 이동할 경로를 설정한다.
                    .invalidateHttpSession(true)// 로그아웃 이후에 세션을 전체 삭제할지 여부를 설정한다.
                .and()
                .csrf().disable() // csrf 비활성화 -> csrf 공격을 방지하기 위해서 활성화하는게 좋지만 실습을 편리하게 하기 위해 비활성화
                .build();
    }

    // 인증 관리자 관련 설정
    // 사용자 정보를 가져올 서비스를 재정의하거나, 인증 방법(ex. LDAP, JDBC 기반) 인증등을 설정할 때 사용
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http , BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService) // 사용자 정보를 가져올 서비스를 설정한다. 이때 설정하는 서비스 클래스는 반드시 UserDetailService를 상속받은 클래스여야 한다.
                .passwordEncoder(bCryptPasswordEncoder) // 비밀번호를 암호화하기 위한 인코더를 설정한다.
                .and()
                .build();
    }

    // 패스워드 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
