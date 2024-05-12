package com.example.demo.module.heart_review.service;

import com.example.demo.module.heart_review.dto.payload.HeartReviewPayload;

public interface HeartReviewService {
    Long save(Long userId, Long reviewId);

    Long delete(Long userId, Long reviewId);

    Boolean checkReviewHeart(Long userId, Long reviewId);

    Integer getHeartCountByReviewId(Long reviewId);

    boolean click(Long reviewId, Long userId);
}
