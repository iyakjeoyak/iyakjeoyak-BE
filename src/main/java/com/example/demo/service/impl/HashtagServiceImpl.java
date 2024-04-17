package com.example.demo.service.impl;

import com.example.demo.domain.repository.HashtagRepository;
import com.example.demo.service.HashtagService;
import com.example.demo.web.result.HashtagResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
    private final HashtagRepository hashtagRepository;


    @Override
    public List<HashtagResult> findAll() {
        return hashtagRepository.findAll().stream().map(h -> HashtagResult.builder().id(h.getId()).name(h.getName()).build()).toList();
    }
}
