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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

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
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final SocialUserRepository socialUserRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MailService mailService;

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

        if (ObjectUtils.isEmpty(image)) {
            throw new IllegalArgumentException("이미지가 생성되지 않았습니다.");
        }

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
                                .hashtag(hashtagRepository.findById(uht).orElseThrow())
                                .user(saveUser)
                                .build()));
        // userRole 저장
        userJoinPayload.getUserRoleList().forEach(
                ur -> userRoleRepository.save(
                        UserRole.builder()
                                .user(saveUser)
                                .role(roleRepository.findById(ur).orElseThrow())
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
        User user = userRepository.findByUsername(userLoginPayload.getUsername()).orElseThrow();

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
    public Long editUser(Long userId, UserEditPayload userEditPayload) {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다."));

        // UserHashtag에 있는 애들 일단 가져오기
        // userhashtag를 변경 하는데.. 단순하게 hashtag로 받아서 쓰고 userhashtag를 저장하자
        userHashTagRepository.deleteAll();

        userEditPayload.getHashtagResultList().forEach(
                ht -> userHashTagRepository.save(UserHashtag
                        .builder()
                        .user(user)
                        .hashtag(hashtagRepository.findById(ht.getId()).orElseThrow())
                        .build()));


        user.editUser(userEditPayload);

        return userId;
    }

    /*
     * 유저 벨리데이션, 인터셉터?
     * */
    @Override
    public UserValidationResult validationUser(Long userId) {

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
    public String  authorizationCodeToGoogle(String code) {

        //TODO 따로 파는 구글

        String client_id = "308885497470-d9ad3gn7me2elbvkl6suc068h96tm20p.apps.googleusercontent.com";
        String redirect_uri = "http://localhost:5173/auth/google";
//        String requestUrl = "https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id="+client_id+"&redirect_uri="+redirect_uri+"&code=" + code;
        String accessToken = "";
        String refreshToken = "";


        return "123";
    }

    @Override
    public String createTokenByGoogleToken(String token) {

        //TODO 따로 파는 구글

        return "123";
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
        if (!mailService.verifyMail(user.getUsername(), verifyCode)) {
            throw new CustomException(MAIL_NOT_VALIFY);
        }
        user.changePassword(passwordEncoder.encode(newPassword));

        return user.getUserId();
    }

    @Override
    @Transactional
    public JwtTokenResult authorizationCodeToKakao(String code) throws IOException, ParseException {
        // 인가코드는 한 번 사용되면 끝
        //kakao request + code
        String client_id = "de8b9223df12fdd44efb37c8c599e22f";
        String grant_type = "authorization_code";
        String redirect_uri = "http://localhost:5173/auth/kakao";
        String requestUrl = "https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id="+client_id+"&redirect_uri="+redirect_uri+"&code=" + code;
        String accessToken = "";
        String refreshToken = "";

        try {
            URL url = new URL(requestUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            BufferedReader bw = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            int responseCode = connection.getResponseCode();
            log.info("resposnecode = {}", responseCode);
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(result);
            accessToken = (String) jsonObject.get("access_token");
            refreshToken = (String) jsonObject.get("refresh_token");

            log.info("access {}", accessToken);
            log.info("refresh {}", refreshToken);

            br.close();
            bw.close();


        }catch (Exception e) {

        }

        JwtTokenResult jwtTokenResult = createTokenByKakaoToken(accessToken);

        return jwtTokenResult;

    }

    @Override
    @Transactional
    public JwtTokenResult createTokenByKakaoToken(String token) {

        JwtTokenResult jwtTokenResult = null;
        String profileImage = "";
        String gender = "";
        String email = "";
        String socialId = "";

        try {
            URL url = new URL("https://kapi.kakao.com/v2/user/me");

            log.info("token {}", token);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setDoOutput(true);

            int responseCode = conn.getResponseCode();
            log.info("response = {}", responseCode);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }


            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(result);
            JSONObject properties = (JSONObject) jsonObject.get("properties");
            JSONObject profiles = (JSONObject) jsonObject.get("kakao_account");
            profileImage = (String) properties.get("profile_image");
            gender = (String) profiles.get("gender");
            email = (String) profiles.get("email");
//            JSONObject id = (JSONObject) jsonObject.get("id");
//            socialId = (String) id.get("id");

        } catch (Exception e) {
        }

//        User findUser = userRepository.findByUsername(email).orElse(null);

        SocialUser findUser = socialUserRepository.findSocialUserBySocialEmail(email).orElse(null);

        if (ObjectUtils.isEmpty(findUser)) {

            // user table도 같이 저장
            User saveUser = User.builder().gender(Gender.valueOf(gender.toUpperCase())).build();
            userRepository.save(saveUser);

            // social user 저장
            SocialUser socialUser = SocialUser.builder().imageUrl(profileImage).socialId(socialId).socialType(SocialType.KAKAO).socialEmail(email).build();

            socialUserRepository.save(socialUser);

            JwtTokenPayload jwtTokenPayload = JwtTokenPayload.builder().userId(saveUser.getUserId()).username(email).build();

            jwtTokenResult = jwtUtil.createAccessAndRefreshToken(jwtTokenPayload);
        } else {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다. 확인하시고 다시 시도해주세요.");
        }


        return jwtTokenResult;
    }

}