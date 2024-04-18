package com.example.demo.module.review.service;

import com.example.demo.module.review.dto.payload.ReviewEditPayload;
import com.example.demo.module.review.dto.payload.ReviewPayload;
import com.example.demo.module.review.dto.result.ReviewResult;
import com.example.demo.module.common.result.PageResult;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;

public interface ReviewService {
    Long save(Long userId,ReviewPayload reviewPayload);

    ReviewResult findOneByReviewId(Long reviewId);

    Long editReview(Long userId , Long reviewId, ReviewEditPayload reviewEditPayload);

    Long deleteByReviewId(Long userId , Long reviewId);

    PageResult<ReviewResult> findPageByMedicineId(Long medicineId, PageRequest pageRequest);
}
