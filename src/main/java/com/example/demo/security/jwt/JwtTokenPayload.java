package com.example.demo.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JwtTokenPayload {

    //TODO 추후 더 추가될 예정
    private Long userId;
    private String username;
    private String nickname;

}
