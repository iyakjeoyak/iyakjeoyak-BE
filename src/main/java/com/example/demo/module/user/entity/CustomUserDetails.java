package com.example.demo.module.user.entity;

import com.example.demo.security.jwt.JwtTokenPayload;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@Builder
@RequiredArgsConstructor
public class CustomUserDetails implements Authentication {

    private final JwtTokenPayload jwtTokenPayload;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    // principal = username (ID)
    // JWT ->
    @Override
    public Object getPrincipal() {
        return jwtTokenPayload.getUserId();
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return null;
    }

}
