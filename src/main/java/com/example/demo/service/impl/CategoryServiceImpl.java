package com.example.demo.service.impl;

import com.example.demo.domain.repository.CategoryRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.util.mapper.CategoryResultMapper;
import com.example.demo.web.result.CategoryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryResultMapper categoryResultMapper;

    @Override
    public List<CategoryResult> findAll() {
        return categoryResultMapper.toDtoList(categoryRepository.findAll());
    }
}
