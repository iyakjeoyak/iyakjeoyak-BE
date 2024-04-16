package com.example.demo.service.impl;

import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.web.payload.UserJoinPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    //TODO Bcrpt
    private final PasswordEncoder passwordEncoder;
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
}
