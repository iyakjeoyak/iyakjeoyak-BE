package com.example.demo.security.jwt;

import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.ErrorResult;
import com.example.demo.module.user.entity.CustomUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return Arrays.stream(jwtUtil.allowedUrls).anyMatch(item -> item.equalsIgnoreCase(request.getServletPath())) || Arrays.stream(jwtUtil.onlyGetNotFilter).anyMatch(item -> request.getMethod().equals("GET") && request.getServletPath().startsWith(item)); // true면 fileter 안 탐
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (!token.equals("null") && !StringUtils.isEmpty(token)) {
                if (jwtUtil.validateToken(token)) {

                    JwtTokenPayload jwtTokenPayload = jwtUtil.getJwtTokenPayload(token);

                    if (ObjectUtils.isNotEmpty(jwtTokenPayload)) {
                        CustomUserDetails customUserDetails = new CustomUserDetails(jwtTokenPayload);

                        SecurityContextHolder.getContext().setAuthentication(customUserDetails);
                    }
                } else {
                    setErrorResponse(response, ErrorCode.JWT_TYPE_ERROR);
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    public static void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        ResponseEntity<ErrorResult> errorResult = ErrorResult.ofResponse(errorCode);
        response.setStatus(errorResult.getStatusCode().value());

        ObjectMapper om = new ObjectMapper();
        String body = om.writeValueAsString(errorResult.getBody());

        response.getWriter().write(body);
    }
}
