package com.example.demo.security.jwt;

import com.example.demo.global.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static com.example.demo.security.jwt.JwtFilter.setErrorResponse;

@RequiredArgsConstructor
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return Arrays.stream(jwtUtil.allowedUrls).anyMatch(item -> item.equalsIgnoreCase(request.getServletPath())) || Arrays.stream(jwtUtil.onlyGetNotFilter).anyMatch(item -> request.getMethod().equals("GET") && request.getServletPath().startsWith(item)); // true면 fileter 안 탐
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {

            log.info("[JwtExceptionFilter] : 토큰 만료");
            setErrorResponse(response, ErrorCode.JWT_TIME_EXP);

        } catch (JwtException | IllegalArgumentException e) {

            log.info("[JwtExceptionFilter] : 유효하지 않은 토큰");
            setErrorResponse(response, ErrorCode.JWT_INVALID);

        } catch (NoSuchElementException e) {

            log.info("[JwtExceptionFilter] : 유저 정보 찾지 못함");
            setErrorResponse(response, ErrorCode.USER_NOT_FOUND);

        } catch (ArrayIndexOutOfBoundsException e) {

            log.info("[JwtExceptionFilter] : 배열 범위 벗어남 (권한 등)");
            setErrorResponse(response, ErrorCode.JWT_INVALID);

        } catch (NullPointerException e) {

            filterChain.doFilter(request, response);
        }
    }
}
