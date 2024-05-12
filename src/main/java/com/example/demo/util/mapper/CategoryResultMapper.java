package com.example.demo.util.mapper;

import com.example.demo.module.category.entity.Category;
import com.example.demo.module.category.dto.result.CategoryResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryResultMapper extends EntityMapper<CategoryResult, Category> {
}
