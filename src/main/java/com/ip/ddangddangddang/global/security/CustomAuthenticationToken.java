package com.ip.ddangddangddang.global.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 611L;
    private final Object principal;
    private Object credentials; //자격증명

    public CustomAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
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
