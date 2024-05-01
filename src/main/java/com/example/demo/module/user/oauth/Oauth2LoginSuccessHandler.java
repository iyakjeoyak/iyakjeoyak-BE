package com.example.demo.module.user.oauth;

import com.example.demo.module.user.repository.UserRepository;
import com.example.demo.security.jwt.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

//    private static String URL = "domain/fhhsdjhdsfjhdsf//fsdfsd";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        try {// auth USER
/*            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            //TODO 토큰 발급 및 토큰 벨리데이션 리프레시 토큰
            //TODO 여기서 만들면 될듯?
//            jwtUtil.createAccessToken()

                log.info("oAuth2User", oAuth2User);
            // 요청 response
            // url 요청
//            String 토큰 = UriComponentsBuilder.fromUriString(URL).queryParam("토큰").build().toUriString();

            // token 발급
                response.sendRedirect(토큰);*/

            CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = customOAuth2User.getAttributes();
            Object kakaoAccount = customOAuth2User.getAttribute("kakao_account");
            String email = customOAuth2User.getEmail();
            log.info("kakaoAccount {}", kakaoAccount);
            for (String key : attributes.keySet()){
                System.out.println("key : " + key + " / " + "value : " + attributes.get(key));
            }

//            log.info("attributess {}", );
            log.info("authentication {}", authentication);
            log.info("authentication.details {}", authentication.getDetails());
            log.info("authentication.principal {}", authentication.getPrincipal());
            log.info("authentication.authorites {}", authentication.getAuthorities());
            log.info("authentication.credential {}", authentication.getCredentials());
            log.info("authentication.getname {}", authentication.getName());


        } catch (Exception e) {
            throw e;
        }

    }
}
