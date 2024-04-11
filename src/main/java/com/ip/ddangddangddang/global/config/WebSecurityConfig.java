package com.ip.ddangddangddang.global.config;

import com.ip.ddangddangddang.domain.user.repository.UserRepository;
import com.ip.ddangddangddang.global.jwt.JwtUtil;
import com.ip.ddangddangddang.global.security.JwtAuthenticationFilter;
import com.ip.ddangddangddang.global.security.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // AuthenticationManager = 인증 처리하는 스프링 시큐리티 빈
    // 핵심 컴포넌트 중 하나
    // AuthenticationConfiguration 객체 = 스프링 시큐리티의 인증 구성을 위해 제공됨
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception {
        return configuration.getAuthenticationManager();
    }


    // 로그인, jwt생성
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil, userRepository,
            passwordEncoder);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    // jwt 검증
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 보안 - 보호 설정 비활성화
        http.csrf((csrf) -> csrf.disable());

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
            authorizeHttpRequests
                // 정적 리소스(예: CSS, JavaScript, 이미지 파일 등)는 보안 검사를 거치지 않고 누구나 접근할 수 있도록 허용
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                .requestMatchers("/**").permitAll() // 메인 페이지 요청 허가
//                .requestMatchers("/login").permitAll() // 메인 페이지 요청 허가
//                .requestMatchers("/signup").permitAll() // 메인 페이지 요청 허가
//                .requestMatchers("/v1/users/signin").permitAll() // 회원가입,로그인 요청 모두 접근 허가
//                .requestMatchers("/v1/users/signup").permitAll()
                .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

        //필터 관리
        // 필터 순서 = 1. jwt검증 2. 로그인, jwt생성 3. 사용자가 제출한 인증 정보를 검사하고, 이를 기반으로 사용자를 인증하는 필터
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}