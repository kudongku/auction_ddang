package com.ip.ddangddangddang.global.security;

import com.ip.ddangddangddang.domain.user.entity.User;
import com.ip.ddangddangddang.global.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
        HttpServletRequest req,
        HttpServletResponse res,
        FilterChain filterChain
    ) throws ServletException, IOException {

        // 순수한 토큰 값
        String tokenValue = jwtUtil.getJwtFromHeader(req);
        // 토큰이 비어 있는지 확인
        if (StringUtils.hasText(tokenValue)) {

            try {
                Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
                Long userId = info.get("userId", Long.class);
                String email = info.getSubject();
                setAuthentication(userId, email);
            } catch (SecurityException | MalformedJwtException | SignatureException e) {
                log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
                return;
            } catch (ExpiredJwtException e) {
                log.error("Expired JWT token, 만료된 JWT token 입니다.");
                return;
            } catch (UnsupportedJwtException e) {
                log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
                return;
            } catch (IllegalArgumentException e) {
                log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
                return;
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }

        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(Long userId, String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(userId, email);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(Long userId, String email) {
        User user = new User(userId, email);
        UserDetails userDetails = new UserDetailsImpl(user);

        return new CustomAuthentication(userDetails);
    }

}
