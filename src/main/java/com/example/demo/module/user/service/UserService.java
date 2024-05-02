package com.example.demo.module.user.service;

import com.example.demo.module.hashtag.dto.result.HashtagResult;
import com.example.demo.module.hashtag.entity.Hashtag;
import com.example.demo.module.user.dto.payload.UserEditPayload;
import com.example.demo.module.user.dto.payload.UserJoinPayload;
import com.example.demo.module.user.dto.payload.UserLoginPayload;
import com.example.demo.module.user.dto.result.UserResult;
import com.example.demo.module.user.dto.result.UserValidationResult;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.user.entity.UserHashtag;
import com.example.demo.security.jwt.JwtTokenResult;
import org.json.simple.parser.ParseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface UserService {
    Long createUser(UserJoinPayload userJoinPayload, MultipartFile imgFile) throws IOException;

    JwtTokenResult loginUser(UserLoginPayload userLoginPayload);

    UserResult findOneByUserId(Long userId);

    Long deleteByUserId(Long userId);

    Long editUser(Long userId, UserEditPayload userEditPayload);

    UserValidationResult validationUser(Long userId);

    String createAccessByRefresh(String refreshToken);

    Boolean checkDuplicateUsername(String username);

    Boolean checkDuplicateNickname(String nickname);

    String authorizationCodeToKakao(String code) throws IOException, ParseException;

    String createTokenByKakaoToken(String token);

    String authorizationCodeToGoogle(String code);

    String createTokenByGoogleToken(String token);
}
