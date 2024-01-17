package com.spring.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { // 요청마다 필터를 거치게 한다.

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization"); // 헤더에서 Authorization 값을 가져온다.
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) { // 헤더에 Authorization 값이 없거나 Bearer 로 시작하지 않으면
            filterChain.doFilter(request, response); // 필터를 거치지 않고 다음 필터로 넘어간다. (다음 필터에 전달)
            return;
        }

        jwt = authHeader.substring(7); // Bearer 뒤에 있는 토큰 값을 가져온다.
        userEmail = jwtService.extractUsername(jwt);


    }
}
