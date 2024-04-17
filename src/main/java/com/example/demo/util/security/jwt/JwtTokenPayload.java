package com.example.demo.util.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtTokenPayload {

    private Long userId;
    private String username;
}
