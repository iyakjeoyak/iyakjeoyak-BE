package com.example.demo.security.jwt;

import com.example.demo.module.user.entity.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
//TODO JWT extends OncePerRequestFilter, Jwtprovider=  jwtutil,   // usernameauthentication -> queryparameter
//TODO keyword jwtfilter, jwtutil // Authentication 세팅
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    /*
     * JWT &#xD1A0;&#xD070; &#xAC80;&#xC99D; &#xD544;&#xD130; &#xC218;&#xD589;
     *
     * */

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        return Arrays.stream(jwtUtil.allowedUrls).anyMatch(item -> item.equalsIgnoreCase(request.getServletPath())) && Arrays.stream(jwtUtil.onlyGetAllow).anyMatch(item -> request.getMethod().equals("GET") && item.equalsIgnoreCase(request.getServletPath())); // true면 fileter 안 탐
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request 헤더에서 Authorization 정보 가져오기
        String authorizationHeader = request.getHeader("Authorization");
        // access, ref
        // "null"

        // JWT가 헤더에 ㅣㅇㅆ는 경우 Bearer가 붙여있는 녀석 가져오기
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Bearer 자르기
            String token = authorizationHeader.substring(7);
            if (!token.equals("null") && !StringUtils.isEmpty(token)) {
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
                } else {
//                    SecurityContextHolder.clearContext();
                    response.sendError(401);
                    return;
                }
            }
        }

        // 다음 필터로 진행 시키자
        filterChain.doFilter(request, response);
    }
}
