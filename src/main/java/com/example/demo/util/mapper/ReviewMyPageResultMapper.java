package com.example.demo.util.mapper;

import com.example.demo.module.review.dto.result.ReviewMyPageResult;
import com.example.demo.module.review.entity.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMyPageResultMapper extends EntityMapper<ReviewMyPageResult, Review>{
}
