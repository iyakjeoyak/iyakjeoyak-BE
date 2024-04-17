package com.example.demo.module.user.dto.payload;

import lombok.Data;

@Data
public class UserLoginPayload {

    private Long userId;
    private String username;
    private String password;

}
