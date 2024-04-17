package com.example.demo.service.impl;

import com.example.demo.domain.repository.CategoryRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.web.result.CategoryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;


    @Override
    public List<CategoryResult> findAll() {
        return
                categoryRepository.findAll().stream().map(c ->
                        CategoryResult.builder()
                                .id(c.getId())
                                .name(c.getName())
                                .build()
                ).toList();
    }
}
