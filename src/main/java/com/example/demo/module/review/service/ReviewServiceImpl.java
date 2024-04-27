package com.example.demo.module.review.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.hashtag.repository.HashtagRepository;
import com.example.demo.module.image.entity.Image;
import com.example.demo.module.image.entity.ReviewImage;
import com.example.demo.module.image.repository.ReviewImageRepository;
import com.example.demo.module.image.service.ImageService;
import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.medicine.repository.MedicineRepository;
import com.example.demo.module.point.service.PointHistoryService;
import com.example.demo.module.review.dto.payload.ReviewEditPayload;
import com.example.demo.module.review.dto.payload.ReviewPayload;
import com.example.demo.module.review.dto.result.ReviewMyPageResult;
import com.example.demo.module.review.dto.result.ReviewResult;
import com.example.demo.module.review.entity.Review;
import com.example.demo.module.review.entity.ReviewHashtag;
import com.example.demo.module.review.repository.ReviewHashtagRepository;
import com.example.demo.module.review.repository.ReviewRepository;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.user.repository.UserRepository;
import com.example.demo.util.mapper.ReviewMapper;
import com.example.demo.util.mapper.ReviewMyPageResultMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

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

    //테스트를 위한 setter
    @Setter
    @Value("${point.review}")
    private Integer reviewCreatePoint;

    @Override
    @Transactional
    public Long save(Long userId, ReviewPayload reviewPayload) throws IOException {

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (reviewRepository.existsByMedicineIdAndCreatedByUserId(reviewPayload.getMedicineId(), user.getUserId())) {
            throw new IllegalArgumentException("이미 후기를 작성한 영양제 입니다.");
        }
        Medicine medicine = medicineRepository.findById(reviewPayload.getMedicineId()).orElseThrow(() -> new CustomException(ErrorCode.MEDICINE_NOT_FOUND));

        Review review = reviewRepository.save(
                Review.builder()
                        .title(reviewPayload.getTitle())
                        .content(reviewPayload.getContent())
                        .star(reviewPayload.getStar())
                        .heartCount(0)
                        .medicine(medicine)
                        .build());

        List<Image> images = imageService.saveImageList(reviewPayload.getImgList());

        images.forEach(i ->
                reviewImageRepository.save(
                        ReviewImage.builder()
                                .review(review)
                                .image(i).build()));

        reviewPayload.getTagList().forEach(
                ht -> reviewHashtagRepository.save(
                        ReviewHashtag.builder()
                                .review(review)
                                .hashtag(hashtagRepository.findById(ht).orElseThrow())
                                .build()));

        pointHistoryService.savePointHistory(REVIEW, RESERVE, reviewCreatePoint, user.reviewPoint(reviewCreatePoint), review.getId());

        return review.getId();
    }

    @Override
    public ReviewResult findOneByReviewId(Long reviewId) {
        return reviewMapper.toDto(
                reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND)));
    }

    //이미지 수정은 따로 뺼 예정
    @Override
    @Transactional
    public Long editReview(Long userId, Long reviewId, ReviewEditPayload reviewEditPayload) {
        if (!reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND))
                .getCreatedBy().getUserId().equals(userId)) {
            throw new IllegalArgumentException("후기 작성자만 수정할 수 있습니다.");
        }
        //없어진 해쉬태그 삭제
        reviewHashtagRepository.findAllByReviewId(reviewId).forEach(ht -> {
            if (!reviewEditPayload.getTagList().contains(ht)) {
                reviewHashtagRepository.delete(ht);
            }
        });

        // 새로 추가된 hashtag 테이블 생성
        reviewEditPayload.getTagList().forEach(ht -> {
            if (reviewHashtagRepository.findByReviewIdAndHashtagId(reviewId, ht).orElse(null) == null) {
                reviewHashtagRepository.save(
                        ReviewHashtag.builder()
                                .review(reviewRepository.findById(reviewId).orElseThrow())
                                .hashtag(hashtagRepository.findById(ht).orElseThrow())
                                .build());
            }
        });

        Review review = reviewRepository.findById(reviewId).orElseThrow();
        return review.update(reviewEditPayload);
    }

    @Transactional
    @Override
    public Long deleteByReviewId(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        if (!review.getCreatedBy().getUserId().equals(userId)) {
            throw new IllegalArgumentException("후기 작성자만 삭제할 수 있습니다.");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Integer minusPoint = user.minusPoint(reviewCreatePoint);
        // 포인트 삭감 메서드
        pointHistoryService.saveDeletePointHistory(REVIEW, CANCELED, reviewCreatePoint * (-1), minusPoint, reviewId);

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

    @Transactional
    @Override
    public Long deleteReviewImage(Long userId, Long reviewId, Long imageId) {
        if (reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND))
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
        if (reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND))
                .getCreatedBy().getUserId().equals(userId)) {
            throw new IllegalArgumentException("리뷰 작성자만 이미지 추가 가능합니다.");
        }
        List<Image> images = imageService.saveImageList(img);

        images.forEach(i ->
                reviewImageRepository.save(
                        ReviewImage.builder()
                                .review(reviewRepository.findById(reviewId).orElseThrow(()-> new CustomException(ErrorCode.REVIEW_NOT_FOUND)))
                                .image(i).build()));

        return reviewId;
    }
}
