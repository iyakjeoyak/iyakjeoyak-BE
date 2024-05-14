package com.example.demo.module.review.service;

import com.example.demo.module.review.dto.payload.ReviewEditPayload;
import com.example.demo.module.review.dto.payload.ReviewPayload;
import com.example.demo.module.review.dto.result.ReviewDetailResult;
import com.example.demo.module.review.dto.result.ReviewMyPageResult;
import com.example.demo.module.review.dto.result.ReviewResult;
import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.review.dto.result.ReviewSimpleMyPageResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReviewService {
    Long save(Long userId, ReviewPayload reviewPayload, List<MultipartFile> imgFile) throws IOException;

    ReviewDetailResult findOneByReviewId(Long reviewId, Long userId);

    Long editReview(Long userId, Long reviewId, ReviewEditPayload reviewEditPayload);

    Long deleteByReviewId(Long userId, Long reviewId);

    PageResult<ReviewResult> findPageByMedicineId(Long medicineId, PageRequest pageRequest);

    PageResult<ReviewMyPageResult> findPageByUserId(Long userId, PageRequest of);

    List<ReviewSimpleMyPageResult> findSimpleResultPageByUserId(Long userId, PageRequest of);

    Long deleteReviewImage(Long userId, Long reviewId, Long imageId);

    Long addReviewImage(Long userId, Long reviewId, List<MultipartFile> img) throws IOException;

    List<ReviewDetailResult> findTopReview(int size);
}
