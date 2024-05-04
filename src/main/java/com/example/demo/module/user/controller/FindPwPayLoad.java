package com.example.demo.module.user.controller;

import lombok.Data;

@Data
public class FindPwPayLoad {
    private String email;
    private String authCode;
    private String newPassword;
}
