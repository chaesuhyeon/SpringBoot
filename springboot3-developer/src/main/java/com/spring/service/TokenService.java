package com.spring.service;

import com.spring.config.jwt.TokenProvider;
import com.spring.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * 리프레시 토큰을 전달받아 검증하고,  유효한 리프레시 토큰이라면 새로운 액세스 토큰을 생성하는 토큰 API를 구현한다.
 */
@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    /**
     * 전달받은 리프레시 토큰으로 토큰 유효성 검사를 진행하고, 유효한 토큰인 때 리프레시 토큰으로 사용자 ID를 찾는다.
     * 마지막으로 사용자 ID로 사용자를 찾은 후에 토큰 제공자의 generateToken()메서드를 호출해서 새로운 액세스 토큰을 생성한다.
     */
    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        return tokenProvider.generatedToken(user, Duration.ofHours(2));
    }
}
