package com.example.demo.module.user.service;

import com.example.demo.module.user.dto.payload.UserJoinPayload;
import com.example.demo.module.user.dto.payload.UserLoginPayload;

public interface UserService {
    public Long createUser(UserJoinPayload userJoinPayload) throws Exception;

    String loginUser(UserLoginPayload userLoginPayload);
}
