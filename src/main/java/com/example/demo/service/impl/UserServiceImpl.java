package com.example.demo.service.impl;

import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.jwt.JwtTokenPayload;
import com.example.demo.util.jwt.JwtUtil;
//import com.example.demo.util.mapper.UserMapper;
import com.example.demo.web.payload.UserJoinPayload;
import com.example.demo.web.payload.UserLoginPayload;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
//    private final UserMapper userMapper;

        public Long createUser(UserJoinPayload userJoinPayload) {
            Boolean isExist = userRepository.existsByUsername(userJoinPayload.getUsername());

            if (isExist) {
                throw new IllegalArgumentException("이미 있는 아이디입니다.");
            }

            User saveUser = userRepository.save(User.builder().username(userJoinPayload.getUsername())
                    .password(passwordEncoder.encode(userJoinPayload.getPassword()))
                    .gender(userJoinPayload.getGender())
                    .nickName(userJoinPayload.getNickName())
                    .age(userJoinPayload.getAge())
                    .build());

            return saveUser.getUserId();
        }

    @Override
    public String loginUser(UserLoginPayload userLoginPayload) {
        String password = userLoginPayload.getPassword();
        User user = userRepository.findByUsername(userLoginPayload.getUsername());

        if (ObjectUtils.isEmpty(user)) {
            throw new IllegalArgumentException("아이디가 존재하지 않습니다.");
        }

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }


        String accessToken = jwtUtil.createAccessToken(new JwtTokenPayload(user.getUserId(), user.getUsername()));

        return accessToken;
    }


}
