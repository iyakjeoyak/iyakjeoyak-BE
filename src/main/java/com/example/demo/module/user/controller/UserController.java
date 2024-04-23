package com.example.demo.module.user.controller;

import com.example.demo.module.user.entity.CustomUserDetails;
import com.example.demo.module.user.service.UserService;
import com.example.demo.module.user.dto.payload.UserJoinPayload;
import com.example.demo.module.user.dto.payload.UserLoginPayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "(유저)", description = "유저 API")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // TODO 고민중이다 비밀번호 확인을 만들 것인가? 내가 봤을 때는 만드는 것이 좋을 것 같다

    @PostMapping
    @Operation(summary = "유저 생성", description = "유저 생성")
    public ResponseEntity<Long> createUser(@RequestBody UserJoinPayload userJoinPayload) {
        return new ResponseEntity<>(userService.createUser(userJoinPayload), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "유저 로그인", description = "유저 로그인 및 JWT 생성")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginPayload userLoginPayload) {
        // 토큰 생성 시작
        // jwt username, nicknmame
        String token = userService.loginUser(userLoginPayload);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    // Authentication 객체가 잘 생성되어 있는 지 확인하는 테스트 컨트롤러
    @GetMapping("/test")
    public void getToken(@AuthenticationPrincipal Long userId) {
        System.out.println(userId);
    }

}