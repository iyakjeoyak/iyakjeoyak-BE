package com.example.demo.module.heart_review.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.module.review.entity.Review;
import com.example.demo.module.heart_review.entity.HeartReview;
import com.example.demo.module.heart_review.repository.HeartReviewRepository;
import com.example.demo.module.review.repository.ReviewRepository;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.user.repository.UserRepository;
import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Objects;

import static com.example.demo.global.exception.ErrorCode.*;
import static com.example.demo.module.point.entity.PointDomain.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HeartReviewServiceImpl implements HeartReviewService {
    private final HeartReviewRepository heartReviewRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Counted("my.heart.review")
    @Transactional
    @Override
    public Long save(Long userId, Long reviewId) {
        if (checkReviewHeart(userId, reviewId)) {
            throw new CustomException(REVIEW_HEART_EXIST);
        }
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));
        review.addHeartCount();

        return heartReviewRepository.save(
                HeartReview.builder()
                        .user(userRepository.findById(userId).orElseThrow(()-> new CustomException(USER_NOT_FOUND)))
                        .review(review)
                        .build()).getId();
    }

    @Transactional
    @Override
    public Long delete(Long userId, Long reviewId) {
        if (!checkReviewHeart(userId, reviewId)) {
            throw new CustomException(REVIEW_HEART_NOT_EXIST);
        }
        reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND)).decreaseHeartCount();

        Long hrId = heartReviewRepository.findByUserUserIdAndReviewId(userId, reviewId).orElseThrow(() -> new CustomException(REVIEW_HEART_NOT_EXIST)).getId();
        heartReviewRepository.deleteById(hrId);
        return hrId;
    }

    @Override
    public Boolean checkReviewHeart(Long userId, Long reviewId) {
        return heartReviewRepository.existsByUserUserIdAndReviewId(userId, reviewId);
    }

    @Override
    public Integer getHeartCountByReviewId(Long reviewId) {
        return heartReviewRepository.findAllByReviewId(reviewId).size();
    }

    @Counted("my.heart.review")
    @Override
    @Transactional
    public boolean click(Long reviewId, Long userId) {
        User foundUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));
        User user = review.getCreatedBy();
        if (checkReviewHeart(userId, reviewId)){
            review.decreaseHeartCount();
            if(!Objects.equals(user.getUserId(), userId)){
                user.minusPoint(HEART.getPoint());
            }
            Long heartReviewId = heartReviewRepository.findByUserUserIdAndReviewId(userId, reviewId).orElseThrow(() -> new CustomException(REVIEW_HEART_NOT_EXIST)).getId();
            heartReviewRepository.deleteById(heartReviewId);
            return false;
        }
        review.addHeartCount();
        if(!Objects.equals(user.getUserId(), userId)){
            user.reviewPoint(HEART.getPoint());
        }
        heartReviewRepository.save(HeartReview
                .builder()
                .user(foundUser)
                .review(review)
                .build());
        return true;
    }
}
