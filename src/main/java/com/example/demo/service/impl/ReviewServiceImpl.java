package com.example.demo.service.impl;

import com.example.demo.domain.entity.Review;
import com.example.demo.domain.repository.MedicineRepository;
import com.example.demo.domain.repository.ReviewRepository;
import com.example.demo.service.ReviewService;
import com.example.demo.util.mapper.ReviewMapper;
import com.example.demo.web.payload.ReviewEditPayload;
import com.example.demo.web.payload.ReviewPayload;
import com.example.demo.web.result.ReviewResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final MedicineRepository medicineRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public Long save(ReviewPayload reviewPayload) {
        return reviewRepository.save(
                Review.builder()
                        .title(reviewPayload.getTitle())
                        .content(reviewPayload.getContent())
                        .star(reviewPayload.getStar())
                        .heartCount(0)
                        .medicine(medicineRepository.findById(reviewPayload.getMedicineId()).orElseThrow(() -> new NoSuchElementException("해당하는 영양제가 없습니다.")))
                        .build()).getId();
    }

    @Override
    public ReviewResult findOneByReviewId(Long reviewId) {
        return reviewMapper.toDto(
                reviewRepository.findById(reviewId).orElseThrow(() -> new NoSuchElementException("리뷰가 존재하지 않습니다.")));
    }

    @Override
    @Transactional
    public Long editReview(Long reviewId, ReviewEditPayload reviewEditPayload) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        return review.update(reviewEditPayload);
    }

    @Override
    public Long deleteByReviewId(Long reviewId) {
        reviewRepository.deleteById(reviewId);
        return reviewId;
    }
}
