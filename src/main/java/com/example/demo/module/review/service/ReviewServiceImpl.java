package com.example.demo.module.review.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.hashtag.repository.HashtagRepository;
import com.example.demo.module.heart_review.repository.HeartReviewRepository;
import com.example.demo.module.image.entity.Image;
import com.example.demo.module.image.entity.ReviewImage;
import com.example.demo.module.image.repository.ReviewImageRepository;
import com.example.demo.module.image.service.ImageService;
import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.medicine.repository.MedicineRepository;
import com.example.demo.module.point.service.PointHistoryService;
import com.example.demo.module.review.dto.payload.ReviewEditPayload;
import com.example.demo.module.review.dto.payload.ReviewPayload;
import com.example.demo.module.review.dto.result.ReviewDetailResult;
import com.example.demo.module.review.dto.result.ReviewMyPageResult;
import com.example.demo.module.review.dto.result.ReviewResult;
import com.example.demo.module.review.dto.result.ReviewSimpleMyPageResult;
import com.example.demo.module.review.entity.Review;
import com.example.demo.module.review.entity.ReviewHashtag;
import com.example.demo.module.review.repository.ReviewHashtagRepository;
import com.example.demo.module.review.repository.ReviewRepository;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.user.repository.UserRepository;
import com.example.demo.util.mapper.ReviewDetailResultMapper;
import com.example.demo.util.mapper.ReviewMapper;
import com.example.demo.util.mapper.ReviewMyPageResultMapper;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import static com.example.demo.global.exception.ErrorCode.*;
import static com.example.demo.module.point.entity.PointDomain.REVIEW;
import static com.example.demo.module.point.entity.ReserveUse.CANCELED;
import static com.example.demo.module.point.entity.ReserveUse.RESERVE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final MedicineRepository medicineRepository;
    private final ReviewMapper reviewMapper;
    private final ReviewHashtagRepository reviewHashtagRepository;
    private final HashtagRepository hashtagRepository;
    private final UserRepository userRepository;
    private final ReviewMyPageResultMapper reviewMyPageResultMapper;
    private final ImageService imageService;
    private final ReviewImageRepository reviewImageRepository;
    private final PointHistoryService pointHistoryService;
    private final ReviewDetailResultMapper reviewDetailResultMapper;
    private final HeartReviewRepository heartReviewRepository;

    @Timed("my.review")
    @Override
    @Transactional
    public Long save(Long userId, ReviewPayload reviewPayload, List<MultipartFile> imgFile) throws IOException {

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (reviewRepository.existsByMedicineIdAndCreatedByUserId(reviewPayload.getMedicineId(), user.getUserId())) {
            throw new CustomException(REVIEW_DUPLICATION);
        }
        Medicine medicine = medicineRepository.findById(reviewPayload.getMedicineId()).orElseThrow(() -> new CustomException(MEDICINE_NOT_FOUND));

        Review review = reviewRepository.save(
                Review.builder()
                        .title(reviewPayload.getTitle())
                        .content(reviewPayload.getContent())
                        .star(reviewPayload.getStar())
                        .heartCount(0)
                        .medicine(medicine)
                        .build());

        List<Image> images = imageService.saveImageList(imgFile);

        images.forEach(i ->
                reviewImageRepository.save(
                        ReviewImage.builder()
                                .review(review)
                                .image(i).build()));

        reviewPayload.getTagList().forEach(
                ht -> reviewHashtagRepository.save(
                        ReviewHashtag.builder()
                                .review(review)
                                .hashtag(hashtagRepository.findById(ht).orElseThrow(() -> new CustomException(HASHTAG_NOT_FOUND)))
                                .build()));

        pointHistoryService.savePointHistory(REVIEW, RESERVE, REVIEW.getPoint(), user.reviewPoint(REVIEW.getPoint()), review.getId());

        return review.getId();
    }

    @Override
    public ReviewDetailResult findOneByReviewId(Long reviewId , Long userId) {
        ReviewDetailResult result = reviewDetailResultMapper.toDto(
                reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND)));
        result.setIsOwner(result.getCreatedBy().getUserId().equals(userId));
        result.setIsHeart(heartReviewRepository.findByUserUserIdAndReviewId(userId, reviewId).isPresent());

        return result;
    }

    //이미지 수정은 따로 뺼 예정
    @Override
    @Transactional
    public Long editReview(Long userId, Long reviewId, ReviewEditPayload reviewEditPayload) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));
        if (!review.getCreatedBy().getUserId().equals(userId)) {
            throw new CustomException(ACCESS_BLOCKED);
        }
        //없어진 해쉬태그 삭제
        reviewHashtagRepository.findAllByReviewId(reviewId).forEach(ht -> {
            if (!reviewEditPayload.getTagList().contains(ht)) {
                reviewHashtagRepository.delete(ht);
            }
        });

        // 새로 추가된 hashtag 테이블 생성
        reviewEditPayload.getTagList().forEach(htId -> {
            if (!reviewHashtagRepository.existsByReviewIdAndHashtagId(reviewId, htId)) {
                reviewHashtagRepository.save(
                        ReviewHashtag.builder()
                                .review(review)
                                .hashtag(hashtagRepository.findById(htId).orElseThrow(() -> new CustomException(HASHTAG_NOT_FOUND)))
                                .build());
            }
        });
        review.update(reviewEditPayload);
        return review.getId();
    }

    @Timed("my.review")
    @Transactional
    @Override
    public Long deleteByReviewId(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));
        if (!review.getCreatedBy().getUserId().equals(userId)) {
            throw new IllegalArgumentException("후기 작성자만 삭제할 수 있습니다.");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Integer minusPoint = user.minusPoint(REVIEW.getPoint());
        // 포인트 삭감 메서드
        pointHistoryService.saveDeletePointHistory(REVIEW, CANCELED, REVIEW.getPoint() * (-1), minusPoint, reviewId);

        // 영양제 평점 구하는 메서드
        reviewRepository.delete(review);
        return reviewId;
    }

    @Override
    public PageResult<ReviewResult> findPageByMedicineId(Long medicineId, PageRequest pageRequest) {
        Page<ReviewResult> result = reviewRepository.findAllByMedicineId(medicineId, pageRequest).map(reviewMapper::toDto);
        return new PageResult<>(result);
    }

    @Override
    public PageResult<ReviewMyPageResult> findPageByUserId(Long userId, PageRequest pageRequest) {
        Page<ReviewMyPageResult> result = reviewRepository.findAllByCreatedByUserId(userId, pageRequest).map(reviewMyPageResultMapper::toDto);
        return new PageResult<>(result);
    }

    @Override
    public List<ReviewSimpleMyPageResult> findSimpleResultPageByUserId(Long userId, PageRequest pageRequest) {
        return reviewRepository.findAllByCreatedByUserId(userId, pageRequest).map(ReviewSimpleMyPageResult::toDto).getContent();
    }

    @Transactional
    @Override
    public Long deleteReviewImage(Long userId, Long reviewId, Long imageId) {
        if (!reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND))
                .getCreatedBy().getUserId().equals(userId)) {
            throw new IllegalArgumentException("리뷰 작성자만 이미지 삭제 가능합니다.");
        }

        /* 이하 이미지 삭제 로직 */
        //S3에서 파일 삭제하는 로직 실행
        imageService.deleteImage(userId, imageId);

        /* 중간 테이블만 삭제 */
        ReviewImage reviewImage = reviewImageRepository.findByReviewIdAndImageId(reviewId, imageId).orElseThrow(() -> new NoSuchElementException("해당 리뷰의 이미지가 아닙니다."));
        reviewImageRepository.delete(reviewImage);

        return reviewId;
    }

    @Transactional
    @Override
    public Long addReviewImage(Long userId, Long reviewId, List<MultipartFile> img) throws IOException {
        if (reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(USER_NOT_FOUND))
                .getCreatedBy().getUserId().equals(userId)) {
            throw new IllegalArgumentException("리뷰 작성자만 이미지 추가 가능합니다.");
        }
        List<Image> images = imageService.saveImageList(img);

        images.forEach(i ->
                reviewImageRepository.save(
                        ReviewImage.builder()
                                .review(reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND)))
                                .image(i).build()));

        return reviewId;
    }

    @Override
    public List<ReviewDetailResult> findTopReview(int size) {
        List<Review> heartCount = reviewRepository.findAll(PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "heartCount"))).getContent();
        return reviewDetailResultMapper.toDtoList(heartCount);
    }

}
