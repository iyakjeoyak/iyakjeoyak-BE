package com.example.demo.module.user.controller;

import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.module.review.dto.result.ReviewSimpleMyPageResult;
import com.example.demo.module.review.service.ReviewService;
import com.example.demo.module.user.dto.payload.FindPwPayLoad;
import com.example.demo.module.user.dto.payload.UserEditPayload;
import com.example.demo.module.user.dto.payload.UserJoinPayload;
import com.example.demo.module.user.dto.payload.UserLoginPayload;
import com.example.demo.module.user.dto.result.ChangePasswordPayLoad;
import com.example.demo.module.user.dto.result.UserDetailResult;
import com.example.demo.module.user.dto.result.UserResult;
import com.example.demo.module.user.dto.result.UserValidationResult;
import com.example.demo.module.user.service.UserService;
import com.example.demo.module.userStorage.dto.payload.StorageOrderField;
import com.example.demo.module.userStorage.dto.result.UserStorageSimpleResult;
import com.example.demo.module.userStorage.service.UserStorageService;
import com.example.demo.security.jwt.JwtTokenResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.example.demo.module.review.dto.payload.ReviewOrderField.CREATED_DATE;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "(유저)", description = "유저 API")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final UserStorageService userStorageService;

    @GetMapping("/getKakaoAuthCode")
    @Operation(summary = "카카오 유저 생성 및 토큰 생성 ", description = "카카오 유저 생성 및 토큰 생성")
    public ResponseEntity<String> getKakaoAuthorizationCode(@RequestParam String code, HttpServletResponse response) throws IOException, ParseException {
        JwtTokenResult token = userService.authorizationCodeToKakao(code);

        setRefreshCookie(response, token.getRefreshToken());
        response.setHeader("Authorization", token.getAccessToken());

        return ResponseEntity.status(HttpStatus.OK).body(token.getAccessToken());
    }

    @GetMapping("/getGoogleAuthCode")
    @Operation(summary = "구글 유저 생성 및 토큰 생성", description = "구글 유저 생성 및 토큰 생성")
    public ResponseEntity<String> getGoogleAuthorizationCode(@RequestParam String code,HttpServletResponse response) {
        JwtTokenResult token = userService.authorizationCodeToGoogle(code);

        setRefreshCookie(response, token.getRefreshToken());
        response.setHeader("Authorization", token.getAccessToken());

        return ResponseEntity.status(HttpStatus.OK).body(token.getAccessToken());
    }

    // TODO 고민중이다 비밀번호 확인을 만들 것인가? 내가 봤을 때는 만드는 것이 좋을 것 같다
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "유저 생성", description = "gender : enum 타입 ('FEMALE','MALE','SECRET')")
    public ResponseEntity<Long> createUser(
            @RequestPart("userJoinPayload") @Valid UserJoinPayload userJoinPayload,
            @RequestPart(value = "imgFile", required = false) MultipartFile imgFile) throws IOException {
        return new ResponseEntity<>(userService.createUser(userJoinPayload, imgFile), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "유저 로그인", description = "유저 로그인 및 JWT 생성")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginPayload userLoginPayload, HttpServletResponse response) {
        // 토큰 생성 시작
        // jwt username, nicknmame
        // 이미 유저 정보를 저장했으니깐 ? 여기서 검증을 하나?
        JwtTokenResult token = userService.loginUser(userLoginPayload);

        setRefreshCookie(response, token.getRefreshToken());
        response.setHeader("Authorization", token.getAccessToken());

        return new ResponseEntity<>(token.getAccessToken(), HttpStatus.OK);
    }


    @PostMapping("/createAccessByRefresh")
    @Operation(summary = "리프레쉬 토큰으로 엑세스 토큰 발급", description = "리프레쉬 토큰으로 엑세스 토큰 발급")
    public ResponseEntity<String> createAccessByRefresh(@CookieValue(value = "refreshToken", required = false) Cookie cookie) {

        String refreshToken = "";
        if (cookie != null) {
            refreshToken = cookie.getValue();
        }

        return new ResponseEntity<>(userService.createAccessByRefresh(refreshToken), HttpStatus.OK);
    }

    @GetMapping("/checkToken")
    @Operation(summary = "토큰 검증", description = "토큰 검증")
    public ResponseEntity<UserValidationResult> validationUser(@AuthenticationPrincipal Long userId) {
        return new ResponseEntity<>(userService.validationUser(userId), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "유저 단건 조회", description = "유저 단건 조회")
    public ResponseEntity<UserDetailResult> findOneByUserId(@AuthenticationPrincipal Long userId) {
        UserResult userResult = userService.findOneByUserId(userId);
        List<ReviewSimpleMyPageResult> simpleResults = reviewService.findSimpleResultPageByUserId(userId, PageRequest.of(0, 3, Sort.Direction.DESC, CREATED_DATE.getValue()));
        List<UserStorageSimpleResult> userStorageList = userStorageService.getAllByUserId(userId, PageRequest.of(0, 4, Sort.Direction.DESC, StorageOrderField.CREATED_DATE.getValue())).getData();
        UserDetailResult userDetailResult = getUserDetailResult(userResult, simpleResults, userStorageList);
        return new ResponseEntity<>(userDetailResult, HttpStatus.OK);
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

    @GetMapping("/check/username/{username}")
    @Operation(summary = "로그인 id 중복체크", description = "로그인 id 중복체크(중복이면 ture)")
    public ResponseEntity<Boolean> checkDuplicateUsername(@PathVariable("username") String username) {
        return new ResponseEntity<>(userService.checkDuplicateUsername(username), HttpStatus.OK);
    }

    @GetMapping("/check/nickname/{nickname}")
    @Operation(summary = "닉네임 중복체크", description = "닉네임 중복체크(중복이면 ture)")
    public ResponseEntity<Boolean> checkDuplicateNickname(@PathVariable("nickname") String nickname) {
        return new ResponseEntity<>(userService.checkDuplicateNickname(nickname), HttpStatus.OK);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<Long> changePassword(@AuthenticationPrincipal Long userId, @RequestBody ChangePasswordPayLoad changePasswordPayLoad) {
        if (!changePasswordPayLoad.getNewPassword().equals(changePasswordPayLoad.getNewPasswordConfirm())) {
            throw new CustomException(ErrorCode.PW_CONFIRM_FAIL);
        }
        return ResponseEntity.status(HttpStatus.OK).body(userService.changePassword(userId, changePasswordPayLoad.getOldPassword(), changePasswordPayLoad.getNewPassword()));
    }

    @PostMapping("/findPassword")
    public ResponseEntity<Long> findPassword(@RequestBody FindPwPayLoad findPwPayLoad) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findPassword(findPwPayLoad.getEmail(), findPwPayLoad.getNewPassword(), findPwPayLoad.getAuthCode()));
    }

    private void setRefreshCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("refreshToken", token);
        // 1day
        cookie.setMaxAge(24 * 60 * 60);

        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private UserDetailResult getUserDetailResult(UserResult userResult, List<ReviewSimpleMyPageResult> reviewList, List<UserStorageSimpleResult> userStorageList) {
        UserDetailResult userDetailResult = new UserDetailResult();
        userDetailResult.setUserResult(userResult);
        userDetailResult.setLatestReviews(reviewList);
        userDetailResult.setFavoriteSupplements(userStorageList);
        return userDetailResult;
    }
}