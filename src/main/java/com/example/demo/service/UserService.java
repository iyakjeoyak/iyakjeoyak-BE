package com.example.demo.service;

import com.example.demo.web.payload.UserJoinPayload;
import com.example.demo.web.payload.UserLoginPayload;

public interface UserService {
    public Long createUser(UserJoinPayload userJoinPayload) throws Exception;

    String loginUser(UserLoginPayload userLoginPayload);
}
