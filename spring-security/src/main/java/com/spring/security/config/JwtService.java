package com.spring.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "66c744833dc1ee9207422128b6216e0c7a2d4ad7503ddba1fb5f46394b173a92";
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // 주어진 토큰에서 subject 정보를 추출하여 반환
    }

    /**
     * 주어진 jwt 토큰에서 특정 claim 정보를 추출한다.
     * 보통 jwt에는 여러 클레임이 포함되어 있고, 이 중에서 특정 클레임을 추출하기 위한 메서드 생성
     * Function<Claims, T> ClaimsResolver : Claims 객체를 파라미터로 받아서 T 타입의 객체를 반환하는 함수형 인터페이스 (Claims 객체에서 원하는 정보를 추출하여 다른 타입으로 변환하는 로직)
     */
    public <T> T extractClaim(String token, Function<Claims, T> ClaimsResolver) {
        final Claims claims = extractAllClaims(token);
        return ClaimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }


    /**
     * 주어진 토큰이 유효한지 확인한다.
     * 토큰이 만료되었는지, 토큰에 담긴 username과 UserDetails 객체의 username이 일치하는지 확인
     * 토큰이 만료되었는지 확인
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); // 주어진 토큰에서 만료 시간 정보를 추출하여 반환
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims) // 토큰에 담을 정보를 설정
                .setSubject(userDetails.getUsername()) // 토큰에 담을 subject 정보를 설정
                .setIssuedAt(new Date(System.currentTimeMillis())) // 토큰 발행 시간 정보를 설정
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // 토큰 만료 시간 정보를 설정
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // 토큰의 서명을 확인할 때 사용할 key를 설정, 서명 알고리즘도 설정
                .compact(); // 토큰을 생성하고 문자열로 반환

    }

    /**
     *  주어진 jwt 토큰에서 claim 정보를 추출한다.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey()) // 토큰의 서명을 확인할 때 사용할 key를 설정
                .build()
                .parseClaimsJws(token) // 주어진 토큰을 파싱하고 클레임을 추출하여 반환
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // 시크릿 키를 바이트 배열로 디코딩하고,
        return Keys.hmacShaKeyFor(keyBytes); // 해당 바이트 배열을 사용하여 HMAC-SHA-256 알고리즘을 적용한 key를 생성
    }
}
