package com.example.demo.config;

import com.example.demo.module.user.oauth.CustomOAuthUserService;
import com.example.demo.module.user.oauth.Oauth2LoginFailureHandler;
import com.example.demo.module.user.oauth.Oauth2LoginSuccessHandler;
import com.example.demo.security.jwt.JwtUtil;
import com.example.demo.security.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    private final CustomOAuthUserService customOAuthUserService;
    private final Oauth2LoginSuccessHandler successHandler;
    private final Oauth2LoginFailureHandler failureHandler;
    @Bean
    public AuthenticationManager  authenticationManager(AuthenticationConfiguration configuration) throws Exception {

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
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("**").permitAll()
                                .requestMatchers("/admin").hasRole("ADMIN")
                                .anyRequest()
                                .authenticated()

                )
                //세션 관리 상태 없음으로 구성한다, Spring Security가 세션 생성과 사용을 하지 않겠다. 무상태
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//                .oauth2Login(oauth -> oauth.userInfoEndpoint(config -> config.userService(customOAuthUserService)).successHandler(successHandler));
//                .oauth2Login(oauth2Configurer -> oauth2Configurer.loginPage("/login").successHandler(successHandler).failureHandler(failureHandler).userInfoEndpoint(c -> c.userService(customOAuthUserService)));


        return http.build();
    }

    // BcryptPassowrdEncoder 빈 등록
    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
