package com.example.demo.security.jwt;

import com.example.demo.module.user.entity.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
//TODO JWT extends OncePerRequestFilter, Jwtprovider=  jwtutil,   // usernameauthentication -> queryparameter
//TODO keyword jwtfilter, jwtutil // Authentication 세팅
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    /*
    * JWT 토큰 검증 필터 수행
    *
    * */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request 헤더에서 Authorization 정보 가져오기
        String authorizationHeader = request.getHeader("Authorization");

        // JWT가 헤더에 ㅣㅇㅆ는 경우 Bearer가 붙여있는 녀석 가져오기
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Bearer 자르기
            String token = authorizationHeader.substring(7);

            // JWT 유효성 검증
            if (jwtUtil.validateToken(token)) {

                JwtTokenPayload jwtTokenPayload = jwtUtil.getJwtTokenPayload(token);

                // UserDetails가 비어있지 않다면?
                if (ObjectUtils.isNotEmpty(jwtTokenPayload)) {
                    //TODO Authenticaiton 객체 바꾸기
                    // Authentication Token 생성
                    // usernamepasswordAuthenticationToken은 username = principal, password = credential
                    CustomUserDetails customUserDetails = new CustomUserDetails(jwtTokenPayload);

                    // Security context에 저장한다? username password 이
                    SecurityContextHolder.getContext().setAuthentication(customUserDetails);
                }
            }
        }

        // 다음 필터로 진행 시키자
        filterChain.doFilter(request, response);
    }
}
