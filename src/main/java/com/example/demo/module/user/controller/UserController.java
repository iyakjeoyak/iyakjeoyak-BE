package com.example.demo.module.user.controller;

import com.example.demo.module.user.dto.payload.UserEditPayload;
import com.example.demo.module.user.dto.payload.UserJoinPayload;
import com.example.demo.module.user.dto.payload.UserLoginPayload;
import com.example.demo.module.user.dto.result.UserResult;
import com.example.demo.module.user.dto.result.UserValidationResult;
import com.example.demo.module.user.service.UserService;
import com.example.demo.security.jwt.JwtTokenResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "(유저)", description = "유저 API")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // TODO 고민중이다 비밀번호 확인을 만들 것인가? 내가 봤을 때는 만드는 것이 좋을 것 같다
    @PostMapping
    @Operation(summary = "유저 생성", description = "gender : enum 타입 ('FEMALE','MALE','SECRET')")
    public ResponseEntity<Long> createUser(@RequestBody @Valid UserJoinPayload userJoinPayload) throws IOException {
        return new ResponseEntity<>(userService.createUser(userJoinPayload), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "유저 로그인", description = "유저 로그인 및 JWT 생성")
    public ResponseEntity<JwtTokenResult> loginUser(@RequestBody UserLoginPayload userLoginPayload) {
        // 토큰 생성 시작
        // jwt username, nicknmame
        // 이미 유저 정보를 저장했으니깐 ? 여기서 검증을 하나?
        JwtTokenResult token = userService.loginUser(userLoginPayload);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/createAccessByRefresh")
    @Operation(summary = "리프레쉬 토큰으로 엑세스 토큰 발급", description = "리프레쉬 토큰으로 엑세스 토큰 발급")
    public ResponseEntity<String> createAccessByRefresh(@RequestHeader HttpHeaders httpHeaders) {

        String authorization = httpHeaders.get("Auhorization").get(0);

        return new ResponseEntity<>(userService.createAccessByRefresh(authorization), HttpStatus.OK);
    }


    @GetMapping("/checkToken")
    @Operation(summary = "토큰 검증", description = "토큰 검증")
    public ResponseEntity<UserValidationResult> validationUser(@AuthenticationPrincipal Long userId) {
        return new ResponseEntity<>(userService.validationUser(userId), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "유저 단건 조회", description = "유저 단건 조회")
    public ResponseEntity<UserResult> findOneByUserId(@AuthenticationPrincipal Long userId) {
        return new ResponseEntity<>(userService.findOneByUserId(userId), HttpStatus.OK);
    }

    @DeleteMapping
    @Operation(summary = "유저 삭제", description = "유저 삭제")
    public ResponseEntity<Long> deleteUser(@AuthenticationPrincipal Long userId) {
        return new ResponseEntity<>(userService.deleteByUserId(userId), HttpStatus.OK);
    }

    @PatchMapping
    @Operation(summary = "유저 변경", description = "유저 변경")
    public ResponseEntity<Long> editUser(@AuthenticationPrincipal Long userId, @RequestBody UserEditPayload userEditPayload) {
        return new ResponseEntity<>(userService.editUser(userId, userEditPayload), HttpStatus.OK);
    }


}