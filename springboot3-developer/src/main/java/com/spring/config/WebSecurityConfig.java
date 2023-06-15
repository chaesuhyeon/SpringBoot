package com.spring.config;

import com.spring.config.jwt.TokenAuthenticationFilter;
import com.spring.config.jwt.TokenProvider;
import com.spring.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.spring.config.oauth.OAuth2SuccessHandler;
import com.spring.config.oauth.OAuth2UserCustomService;
import com.spring.repository.RefreshTokenRepository;
import com.spring.service.UserDetailService;
import com.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;


//    private final UserDetailService userService;

    // 스프링 시큐리티 기능 비활성화
    // 인증, 인가를 모든 곳에 모두 적용하지 않는다. 일반적으로 정적 리소스(이미지, HTML 파일)에 설정한다.
    // 정적 리소스만 스프링 시큐리티 사용을 비활성화하는데 static 하위 경로에 있는 리소스와 h2 데이터를 확인하는데 사용하는  h2-console 하위 url을 대상으로
    // ignoring() 메서드를 사용한다.
    @Bean
    public WebSecurityCustomizer configure() { // 스프링 시큐리티 기능 비활성화
        return (web -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/img/**", "/css/**", "/js/**"));
    }

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 토큰 방식으로 인증하기 때문에 기존에 사용하던 폼로그인, 세션 비활성화
        http.csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 헤더를 확인할 커스텀 필터 추가
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 토큰 재발급 URL은 인증 없이 접근 가능하도록 설정. 나머지 API URL은 인증 필요
        http.authorizeRequests()
                .requestMatchers("/api/token").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll();

        // OAuth2에 필요한 정보를 세션이 아닌 쿠키에 저장해서 쓸 수 있도록 인증 요청과 관련된 상태를 저장할 저장소를 설정
        http.oauth2Login()
                .loginPage("/login")
                .authorizationEndpoint()
                //Authorization 요청과 관련된 상태 저장
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                .and()
                .successHandler(oAuth2SuccessHandler()) // 인증 성공시 실행할 핸들러
                .userInfoEndpoint()
                .userService(oAuth2UserCustomService);

        http.logout()
                .logoutSuccessUrl("/login");


        // /api로 시작하는 url인 경우 401 상태코드를 반환하도록 예외 처리
        http.exceptionHandling()
                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), new AntPathRequestMatcher("/api/**"));

        return http.build();

    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(tokenProvider, refreshTokenRepository, oAuth2AuthorizationRequestBasedOnCookieRepository(), userService);
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    // 패스워드 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }



    // 기존 시큐리티 설정
    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
/*    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 기존 시큐리티 설정
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

    // 기존 시큐리티 설정
    // 인증 관리자 관련 설정
    // 사용자 정보를 가져올 서비스를 재정의하거나, 인증 방법(ex. LDAP, JDBC 기반) 인증등을 설정할 때 사용
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http , BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService) // 사용자 정보를 가져올 서비스를 설정한다. 이때 설정하는 서비스 클래스는 반드시 UserDetailService를 상속받은 클래스여야 한다.
                .passwordEncoder(bCryptPasswordEncoder) // 비밀번호를 암호화하기 위한 인코더를 설정한다.
                .and()
                .build();
    }*/

}
