package com.example.demo.module.user.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.module.hashtag.repository.HashtagRepository;
import com.example.demo.module.image.entity.Image;
import com.example.demo.module.image.repository.ImageRepository;
import com.example.demo.module.image.service.ImageService;
import com.example.demo.module.user.dto.payload.UserEditPayload;
import com.example.demo.module.user.dto.payload.UserJoinPayload;
import com.example.demo.module.user.dto.payload.UserLoginPayload;
import com.example.demo.module.user.dto.result.UserResult;
import com.example.demo.module.user.dto.result.UserValidationResult;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.user.entity.UserHashtag;
import com.example.demo.module.user.entity.UserRole;
import com.example.demo.module.user.repository.RoleRepository;
import com.example.demo.module.user.repository.UserHashTagRepository;
import com.example.demo.module.user.repository.UserRepository;
import com.example.demo.module.user.repository.UserRoleRepository;
import com.example.demo.security.jwt.JwtTokenPayload;
import com.example.demo.security.jwt.JwtTokenResult;
import com.example.demo.security.jwt.JwtUtil;
import com.example.demo.util.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static com.example.demo.global.exception.ErrorCode.*;


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
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    /*
    * 회원 가입
    * */
    @Transactional
    public Long createUser(UserJoinPayload userJoinPayload) throws IOException {
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

        Image image = imageService.saveImage(userJoinPayload.getProfileImage());

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
    @Override
    public JwtTokenResult loginUser(UserLoginPayload userLoginPayload) {
        // body에서 페이로드로 페스워드 꺼내기
        String password = userLoginPayload.getPassword();
        // username으로 user 테이블 조회
        User user = userRepository.findByUsername(userLoginPayload.getUsername());

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

}