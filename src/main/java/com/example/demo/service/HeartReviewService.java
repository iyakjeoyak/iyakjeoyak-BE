package com.example.demo.service;

import com.example.demo.domain.entity.HeartReview;

import java.util.List;

public interface HeartReviewService {
    Long save(Long userId, Long reviewId) throws IllegalAccessException;

    Long delete(Long userId, Long reviewId) throws IllegalAccessException;

    Boolean checkReviewHeart(Long userId, Long reviewId);

    Integer getHeartCountByReviewId(Long reviewId);
}
