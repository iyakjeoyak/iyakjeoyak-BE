package com.example.demo.util.mapper;

import com.example.demo.domain.entity.Review;
import com.example.demo.web.result.ReviewResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper extends EntityMapper<ReviewResult,Review>{

}
