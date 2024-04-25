package com.example.demo.module.user.controller;

import com.example.demo.module.user.dto.payload.UserEditPayload;
import com.example.demo.module.user.dto.payload.UserJoinPayload;
import com.example.demo.module.user.dto.payload.UserLoginPayload;
import com.example.demo.module.user.dto.result.UserResult;
import com.example.demo.module.user.dto.result.UserValidationResult;
import com.example.demo.module.user.service.UserService;
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
    @Operation(summary = "유저 생성", description = "gender : enum 타입 ('FEMALE','MALE','SECRET')")
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

    //TODO
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