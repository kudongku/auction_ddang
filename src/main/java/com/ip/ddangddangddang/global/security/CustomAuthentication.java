package com.ip.ddangddangddang.global.security;

import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomAuthentication implements Authentication {

    private final UserDetails userDetails;
    private boolean authenticated;

    public CustomAuthentication(UserDetails userDetails) {
        this.userDetails = userDetails;
        this.authenticated = true; // 사용자가 인증된 경우
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }


    @Override
    public Object getCredentials() {
        return null; // 사용자의 자격 증명 정보
    }

    @Override
    public Object getDetails() {
        return null; // 기타 인증 정보
    }

    @Override
    public Object getPrincipal() {
        return userDetails; // 사용자 정보
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    // nickname이 반환됨
    @Override
    public String getName() {
        return userDetails.getUsername(); // 사용자 이름
    }
}