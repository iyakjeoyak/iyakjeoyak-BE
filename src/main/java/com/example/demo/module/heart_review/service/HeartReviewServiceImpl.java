package com.example.demo.module.heart_review.service;

import com.example.demo.module.review.entity.Review;
import com.example.demo.module.heart_review.entity.HeartReview;
import com.example.demo.module.heart_review.repository.HeartReviewRepository;
import com.example.demo.module.review.repository.ReviewRepository;
import com.example.demo.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HeartReviewServiceImpl implements HeartReviewService {
    private final HeartReviewRepository heartReviewRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Long save(Long userId, Long reviewId) {
        if (checkReviewHeart(userId, reviewId)) {
            throw new IllegalArgumentException("이미 좋아요 클릭한 후기입니다.");
        }
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new NoSuchElementException("후기를 찾을 수 없습니다."));
        review.addHeartCount();

        return heartReviewRepository.save(
                HeartReview.builder()
                        .user(userRepository.findById(userId).orElseThrow(()-> new NoSuchElementException("유저 정보를 찾지 못했습니다.")))
                        .review(review)
                        .build()).getId();
    }

    @Transactional
    @Override
    public Long delete(Long userId, Long reviewId) {
        if (!checkReviewHeart(userId, reviewId)) {
            throw new IllegalArgumentException("좋아요 클릭 되지 않은 후기입니다.");
        }
        reviewRepository.findById(reviewId).orElseThrow(() -> new NoSuchElementException("후기를 찾을 수 없습니다.")).decreaseHeartCount();

        Long hrId = heartReviewRepository.findByUserUserIdAndReviewId(userId, reviewId).orElseThrow(() -> new NoSuchElementException("좋아요 클릭되지 않은 후기입니다.")).getId();
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

}
