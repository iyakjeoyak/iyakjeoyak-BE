package com.example.demo.module.user.service;

import com.example.demo.module.user.dto.payload.UserJoinPayload;
import com.example.demo.module.user.dto.payload.UserLoginPayload;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.user.repository.UserRepository;
import com.example.demo.security.jwt.JwtTokenPayload;
import com.example.demo.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public Long createUser(UserJoinPayload userJoinPayload) {
        Boolean isUsernameExist = userRepository.existsByUsername(userJoinPayload.getUsername());
        Boolean isUserNicknameExist = userRepository.existsByNickname(userJoinPayload.getNickname());

        if (isUsernameExist) {
            throw new IllegalArgumentException("이미 있는 아이디입니다.");
        }

        if (isUserNicknameExist) {
            throw new IllegalArgumentException("이미 있는 닉네임입니다.");
        }

        User saveUser = userRepository.save(User.builder().username(userJoinPayload.getUsername())
                .password(passwordEncoder.encode(userJoinPayload.getPassword()))
                .gender(userJoinPayload.getGender())
                .nickname(userJoinPayload.getNickname())
                .age(userJoinPayload.getAge())
                .build());

        return saveUser.getUserId();
    }

    @Override
    public String loginUser(UserLoginPayload userLoginPayload) {
        // body에서 페이로드로 페스워드 꺼내기
        String password = userLoginPayload.getPassword();
        // username으로 user 테이블 조회
        User user = userRepository.findByUsername(userLoginPayload.getUsername());

        //TODO 더 추가할 수도 있음

        // 유효성 검증
        if (ObjectUtils.isEmpty(user)) {
            throw new IllegalArgumentException("아이디가 존재하지 않습니다.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 이전 버전
        // JWT 엑세스 토큰 만들기
        //String accessToken = jwtUtil.createAccessToken(new JwtTokenPayload(user.getUserId(), user.getUsername()));

        // 다음 버전 (디비를 조회할 필요없이 loginPayload로 조회한 녀석으로 JwtTokenPayload 채우기)
        JwtTokenPayload buildPayload = JwtTokenPayload.builder().userId(user.getUserId()).nickname(user.getNickname()).username(user.getUsername()).build();
        // 그대로 토큰을 만드는데 사용하기
        String accessToken = jwtUtil.createAccessToken(buildPayload);

        // 만들어진 토큰 리턴
        return accessToken;
    }


}