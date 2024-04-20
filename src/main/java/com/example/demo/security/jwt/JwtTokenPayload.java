package com.example.demo.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtTokenPayload {

    //TODO 추후 더 추가될 예정
    private Long userId;
    private String username;
}
