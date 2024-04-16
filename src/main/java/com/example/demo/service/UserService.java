package com.example.demo.service;

import com.example.demo.web.payload.UserJoinPayload;

public interface UserService {
    public Long createUser(UserJoinPayload userJoinPayload) throws Exception;
}
