package com.example.demo.web.payload;

import lombok.Data;

@Data
public class UserLoginPayload {

    private Long userId;
    private String username;
    private String password;

}
