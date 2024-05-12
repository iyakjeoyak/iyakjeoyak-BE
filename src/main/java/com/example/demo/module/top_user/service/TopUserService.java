package com.example.demo.module.top_user.service;

import com.example.demo.module.user.dto.result.UserResult;
import com.example.demo.module.user.dto.result.UserSimpleResult;

import java.util.List;

public interface TopUserService {
    List<UserSimpleResult> getTopUsers();
}
