package com.ip.ddangddangddang.global.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 611L;// serialVersion 인터페이스를 구현해야하는데 버전올릴 때 이전에 버전에 토큰을 사용하지 못해 넣는 것(버전관리)
    private final Object principal;
    private Object credentials; //자격증명

    // 주체는 일반적으로 사용자 객체를 나타내고, 자격 증명은 사용자의 비밀번호 또는 다른 인증 정보를 나타냅니다.
    // CustomAuthentication의 setAuthenticated(true)를 호출하여 이 토큰이 인증된 것으로 표시합니다.
    public CustomAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal; //user객체
        this.credentials = credentials; //비밀번호
        super.setAuthenticated(true);
    }


    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
