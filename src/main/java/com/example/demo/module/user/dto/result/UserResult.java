package com.example.demo.module.user.dto.result;

import com.example.demo.module.hashtag.dto.result.HashtagResult;
import com.example.demo.module.image.dto.result.ImageResult;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserResult {

    private Long userId;

    private String username;

    private String gender;

    private Integer age;

    private Integer point;

    private String introduce;

    private List<HashtagResult> hashtagList = new ArrayList<>();

    private ImageResult image;
}
