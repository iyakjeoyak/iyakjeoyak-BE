package com.example.demo.module.user.dto.result;

import com.example.demo.module.user.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@AllArgsConstructor
@Builder
@Data
public class UserValidationResult {

    /*
    * 유저 아디,패스워드 말고 다
    * */

    private String username;

    private String nickname;
    private Gender gender;

    private Integer age;


    //중간 테이블(user + hashtag)
    private Integer hashtagSize;
}
