package com.example.demo.util.mapper;

import com.example.demo.module.review.dto.result.ReviewDetailResult;
import com.example.demo.module.review.entity.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewDetailResultMapper extends EntityMapper<ReviewDetailResult, Review> {
}
