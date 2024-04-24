package com.ip.ddangddangddang.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j(topic = "JwtUtil")
@Component // bean으로 등록
public class JwtUtil {

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // Token 식별자 꼭 붙일 필요는 없지만 규칙
    public static final String BEARER_PREFIX = "Bearer ";

    //토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L;

    @Value("${jwt.secret.key}") //application-.properties에 들어있던 값이 들어감
    private String secretKey;
    //secretKey를 넣을 Key 객체
    private Key key;
    //알고리즘 선택
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct //딱 한번만 받아 오면 되는 값을 받을 때 요청을 새로 받는 걸 방지
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey); //base64로 디코딩
        key = Keys.hmacShaKeyFor(bytes); //key에다가 디코딩한 secretKey를 넣습니다.
    }

    // 토큰 생성
    public String createToken(Long userId, String email) {
        Date date = new Date();

        return BEARER_PREFIX + // bearer 을 앞에 붙어줌
            Jwts.builder()
                .setSubject(email) // 사용자 식별자값 = 로그인 아이디 = 이메일
                .claim("userId", userId)
                .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                .compact();
    }


    // header 에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            // 순수한 토큰이 필요하기 때문에 bearer 을 잘라버림
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        // validateToken에서 검증을 한 토큰의 body를 가져옴 claims라는 데이터의 집합으로 반환함
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

}
