package com.example.demo.module.hashtag.service;

import com.example.demo.module.hashtag.dto.result.HashtagResult;

import java.util.List;

public interface HashtagService {
    List<HashtagResult> findAll();
}
