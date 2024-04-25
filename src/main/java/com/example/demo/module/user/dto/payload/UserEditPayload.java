package com.example.demo.module.user.dto.payload;

import com.example.demo.module.hashtag.dto.result.HashtagResult;
import com.example.demo.module.user.entity.Gender;
import com.example.demo.module.user.entity.UserHashtag;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserEditPayload {

    private Gender gender;

    private String introduce;

    private String nickname;

    private Integer age;

    private List<HashtagResult> userHashtagList = new ArrayList<>();
}
