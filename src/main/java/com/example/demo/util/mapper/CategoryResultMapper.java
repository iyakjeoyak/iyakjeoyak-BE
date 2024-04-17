package com.example.demo.util.mapper;

import com.example.demo.domain.entity.Category;
import com.example.demo.web.result.CategoryResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryResultMapper extends EntityMapper<CategoryResult, Category> {
}
