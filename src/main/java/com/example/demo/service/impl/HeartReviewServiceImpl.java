package com.example.demo.service.impl;

import com.example.demo.domain.entity.HeartReview;
import com.example.demo.domain.repository.HeartReviewRepository;
import com.example.demo.domain.repository.ReviewRepository;
import com.example.demo.service.HeartReviewService;
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

    @Transactional
    @Override
    public Long save(Long userId, Long reviewId){
        if (checkReviewHeart(userId, reviewId)) {
            throw new IllegalArgumentException("이미 좋아요 클릭한 후기입니다.");
        }
        return heartReviewRepository.save(
                HeartReview.builder()
//                        .user(userRepository.findById(userId).orElseThrow())
                        .review(reviewRepository.findById(reviewId).orElseThrow(()->new NoSuchElementException("해당하는 후기가 없습니다.")))
                .build()).getId();
    }

    @Transactional
    @Override
    public Long delete(Long userId, Long reviewId) {
        if (!checkReviewHeart(userId, reviewId)) {
            throw new IllegalArgumentException("좋아요 클릭 되지 않은 후기입니다.");
        }
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
