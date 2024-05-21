package com.example.demo.config;

import com.example.demo.security.jwt.JwtAuthenticationEntryPointHandler;
import com.example.demo.security.jwt.JwtExceptionFilter;
import com.example.demo.security.jwt.JwtFilter;
import com.example.demo.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/*
 * spring security의 인가 및 설정을 담당하는 클래스
 * + spring security context로 봐도 무방할까요?
 * */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtUtil jwtUtil;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((auth) ->
                        auth.disable()
                )
//                .cors(Customizer.withDefaults())
                .formLogin((auth) -> auth.disable())
                .httpBasic((auth) -> auth.disable())
                // usernmaepassword 필터 전에 loginfilter를 거치겠다
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(jwtUtil), JwtFilter.class)
                .exceptionHandling((exceptionConfig) ->
                        exceptionConfig
                                .authenticationEntryPoint(new JwtAuthenticationEntryPointHandler())
                )
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers(jwtUtil.allowedUrls).permitAll()
                                .requestMatchers(HttpMethod.GET, jwtUtil.onlyGetAllowUrl).permitAll()
                                .requestMatchers("/admin").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                //세션 관리 상태 없음으로 구성한다, Spring Security가 세션 생성과 사용을 하지 않겠다. 무상태
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }

    // BcryptPassowrdEncoder 빈 등록
    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
