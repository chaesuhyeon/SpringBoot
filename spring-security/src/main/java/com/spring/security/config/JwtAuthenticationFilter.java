package com.spring.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { // 요청마다 필터를 거치게 한다.

    private final JwtService jwtService;
    private final UserDetailsService userDetailService;

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

        // userEmail이 있고, SecurityContextHolder에 인증된 사용자 정보가 없으면
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailService.loadUserByUsername(userEmail);

            // 토큰이 유효하면 UsernamePasswordAuthenticationToken 객체를 생성
            // 이 객체는 SecurityContextHolder에 저장되어 인증된 사용자로 등록된다.
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                // http 요청에 대한 세부 정보를 설정한다.
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // SecurityContextHolder에 인증된 사용자 정보를 저장한다. (SecurityContextHolder 업데이트)
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response); // 다음 필터로 넘어간다.

    }
}
