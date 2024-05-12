package com.example.demo.module.hashtag.service;

import com.example.demo.module.hashtag.repository.HashtagRepository;
import com.example.demo.util.mapper.HashtagResultMapper;
import com.example.demo.module.hashtag.dto.result.HashtagResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
    private final HashtagRepository hashtagRepository;
    private final HashtagResultMapper hashtagResultMapper;


    @Override
    public List<HashtagResult> findAll() {
        return hashtagResultMapper.toDtoList(hashtagRepository.findAll());
    }
}
