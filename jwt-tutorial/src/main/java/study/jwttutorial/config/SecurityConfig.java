package study.jwttutorial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import study.jwttutorial.jwt.JwtAccessDeniedHandler;
import study.jwttutorial.jwt.JwtAuthenticationEntryPoint;
import study.jwttutorial.jwt.JwtSecurityConfig;
import study.jwttutorial.jwt.TokenProvider;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize 어노테이션을 method 단위로 추가하기 위해 적용
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(
            TokenProvider tokenProvider,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web){
        web
                .ignoring()
                .antMatchers(
                        "/h2-console/**" // h2-console 하위 모든 요청과
                        ,"/favicon.ico" // 파비콘은 모두 무시하는 것으로 설정
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // token 방식을 사용하기 때문에 csrf 설정은 disable

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // h2-console 설정
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // session을 사용하지 않기 때문에 session 설정 STATELESS로 변경
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests() // HttpServletRequest를 사용하는 요청들에 대한 접근 제한을 설정
                .antMatchers("/api/hello").permitAll() // /api/hello에 대한 요청은 인증없이 접근을 허용
                .antMatchers("/api/authenticate").permitAll() // 로그인 api는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .antMatchers("/api/signup").permitAll()// 회원가입 api는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .anyRequest().authenticated() // 그리고 나머지 요청들은 인증을 받아야 함

                // JwtFilter 를 addFilterBefore로 등록했던 JwtSecurityConfig 클래스 추가
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
        }
    }
