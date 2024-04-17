package com.example.demo.module.user.dto.payload;

import lombok.Data;

import java.util.List;

@Data
public class UserJoinPayload {

    private Long userId;

    private String username;

    private String password;

    private String nickName;

    private String gender;

    private Integer age;

    //TODO tag
    private List<Long> tag;

}
