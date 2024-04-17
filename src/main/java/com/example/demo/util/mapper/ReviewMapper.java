package com.example.demo.util.mapper;

import com.example.demo.module.review.entity.Review;
import com.example.demo.module.review.dto.result.ReviewResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper extends EntityMapper<ReviewResult,Review>{

}
