package com.example.demo.module.user.service;

import com.example.demo.module.user.dto.payload.UserJoinPayload;
import com.example.demo.module.user.dto.payload.UserLoginPayload;
import com.example.demo.module.user.dto.result.UserResult;
import com.example.demo.module.user.dto.result.UserValidationResult;
import com.example.demo.module.user.entity.User;

public interface UserService {
    public Long createUser(UserJoinPayload userJoinPayload);

    String loginUser(UserLoginPayload userLoginPayload);

    UserResult findOneByUserId(Long userId);

    Long deleteByUserId(Long userId);
    Long editUser(Long userId);

    UserValidationResult validationUser(Long userId);
}
