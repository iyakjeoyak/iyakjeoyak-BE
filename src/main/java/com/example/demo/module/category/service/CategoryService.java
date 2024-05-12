package com.example.demo.module.category.service;

import com.example.demo.module.category.dto.result.CategoryResult;

import java.util.List;

public interface CategoryService {
    List<CategoryResult> findAll();
}
