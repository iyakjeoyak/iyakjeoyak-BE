package com.example.demo.module.user.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.module.hashtag.repository.HashtagRepository;
import com.example.demo.module.image.entity.Image;
import com.example.demo.module.image.repository.ImageRepository;
import com.example.demo.module.image.service.ImageService;
import com.example.demo.module.mail.service.MailService;
import com.example.demo.module.user.dto.payload.UserEditPayload;
import com.example.demo.module.user.dto.payload.UserJoinPayload;
import com.example.demo.module.user.dto.payload.UserLoginPayload;
import com.example.demo.module.user.dto.result.UserResult;
import com.example.demo.module.user.dto.result.UserValidationResult;
import com.example.demo.module.user.entity.*;
import com.example.demo.module.user.repository.*;
import com.example.demo.security.jwt.JwtTokenPayload;
import com.example.demo.security.jwt.JwtTokenResult;
import com.example.demo.security.jwt.JwtUtil;
import com.example.demo.util.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;

import static com.example.demo.global.exception.ErrorCode.*;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final HashtagRepository hashtagRepository;
    private final UserHashTagRepository userHashTagRepository;
    private final ImageService imageService;
    private final SocialUserRepository socialUserRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MailService mailService;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String GOOGLE_REDIRECT_URL;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URL;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    /*
     * 회원 가입
     * */
    @Counted("my.user")
    @Override
    @Transactional
    public Long createUser(UserJoinPayload userJoinPayload, MultipartFile imgFile) throws IOException {
        Boolean isUsernameExist = userRepository.existsByUsername(userJoinPayload.getUsername());
        Boolean isUserNicknameExist = userRepository.existsByNickname(userJoinPayload.getNickname());

        if (isUsernameExist) {
            throw new IllegalArgumentException("이미 있는 아이디입니다.");
        }

        if (isUserNicknameExist) {
            throw new IllegalArgumentException("이미 있는 닉네임입니다.");
        }

        if (ObjectUtils.notEqual(userJoinPayload.getPassword(), userJoinPayload.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다. 비밀번호를 확인해주세요.");
        }

        Image image = imageService.saveImage(imgFile);

        User saveUser = userRepository.save(User.builder()
                .username(userJoinPayload.getUsername())
                .password(passwordEncoder.encode(userJoinPayload.getPassword()))
                .gender(userJoinPayload.getGender())
                .nickname(userJoinPayload.getNickname())
                .age(userJoinPayload.getAge())
                .image(image)
                .build());
        // UserHash Tag 저장
        // TODD no value present
        userJoinPayload.getUserHashtagList().forEach(
                uht -> userHashTagRepository.save(
                        UserHashtag.builder()
                                .hashtag(hashtagRepository.findById(uht).orElseThrow(() -> new CustomException(HASHTAG_NOT_FOUND)))
                                .user(saveUser)
                                .build()));
        // userRole 저장
        userJoinPayload.getUserRoleList().forEach(
                ur -> userRoleRepository.save(
                        UserRole.builder()
                                .user(saveUser)
                                .role(roleRepository.findById(ur).orElseThrow(() -> new CustomException(ErrorCode.ROLE_NOT_FOUND)))
                                .build()));

        return saveUser.getUserId();
    }

    /*
     * 유저 로그인
     * */
    @Counted("my.user")
    @Override
    public JwtTokenResult loginUser(UserLoginPayload userLoginPayload) {
        // body에서 페이로드로 페스워드 꺼내기
        String password = userLoginPayload.getPassword();
        // username으로 user 테이블 조회
        User user = userRepository.findByUsername(userLoginPayload.getUsername()).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        //TODO 더 추가할 수도 있음

        // 유효성 검증
        if (ObjectUtils.isEmpty(user)) {
            throw new CustomException(USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 다음 버전 (디비를 조회할 필요없이 loginPayload로 조회한 녀석으로 JwtTokenPayload 채우기)
        // TODO 여기서 다 채우기
        JwtTokenPayload buildPayload = JwtTokenPayload.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .build();
        // 그대로 토큰을 만드는데 사용하기
//        String accessToken = jwtUtil.createAccessToken(buildPayload);

        JwtTokenResult accessAndRefreshToken = jwtUtil.createAccessAndRefreshToken(buildPayload);
        // 만들어진 토큰 리턴
        return accessAndRefreshToken;
    }

    @Override
    public UserResult findOneByUserId(Long userId) {

        return userMapper.toDto(
                userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다.")));

    }

    /*
     * 회원 삭제
     * */
    @Transactional
    @Override
    public Long deleteByUserId(Long userId) {

        userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        userRepository.deleteById(userId);

        return userId;
    }


    /*
     * 회원 수정
     * */
    //TODO edit 개발
    @Transactional
    @Override
    public Long editUser(Long userId, UserEditPayload userEditPayload, MultipartFile imgFile) throws IOException {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다."));

        // UserHashtag에 있는 애들 일단 가져오기
        // userhashtag를 변경 하는데.. 단순하게 hashtag로 받아서 쓰고 userhashtag를 저장하자

        List<UserHashtag> allById = userHashTagRepository.findAllByUser(user);

        userHashTagRepository.deleteAll(allById);

        userEditPayload.getHashtagResultList().forEach(
                ht -> userHashTagRepository.save(UserHashtag
                        .builder()
                        .user(user)
                        .hashtag(hashtagRepository.findById(ht).orElseThrow(() -> new CustomException(HASHTAG_NOT_FOUND)))
                        .build()));
        Image image = imageService.saveImage(imgFile);

        if (ObjectUtils.isNotEmpty(image)) {
            user.changeImage(image);
        }

        user.editUser(userEditPayload);

        return userId;
    }

    /*
     * 유저 벨리데이션, 인터셉터?
     * */
    @Override
    public UserValidationResult validationUser(Long userId) {
        if (userId == null) {
            throw new CustomException(USER_NOT_FOUND);
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다."));

        return UserValidationResult.builder()
                .age(user.getAge())
                .gender(user.getGender())
                .hashtagSize(user.getUserHashTagList().size())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .build();
    }

    @Override
    public String createAccessByRefresh(String refreshToken) {

        Claims claims = jwtUtil.parseClaims(refreshToken);
        JwtTokenPayload tokenPayload = JwtTokenPayload.builder()
                .userId(claims.get("userId", Long.class))
                .username(claims.get("username", String.class))
                .nickname(claims.get("nickname", String.class))
                .build();

        return jwtUtil.createAccessToken(tokenPayload);
    }

    @Override
    public Boolean checkDuplicateUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean checkDuplicateNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Override
    public Long changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        if (!passwordEncoder.matches(user.getPassword(), oldPassword)) {
            throw new CustomException(PW_NOT_MATCH);
        }
        user.changePassword(passwordEncoder.encode(newPassword));
        return user.getUserId();
    }

    @Override
    public Long findPassword(String username, String newPassword, String verifyCode) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        user.changePassword(passwordEncoder.encode(newPassword));

        return user.getUserId();
    }



}