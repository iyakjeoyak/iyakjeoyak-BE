package com.example.demo.module.review.service;

import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.hashtag.repository.HashtagRepository;
import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.medicine.repository.MedicineRepository;
import com.example.demo.module.point.entity.PointHistory;
import com.example.demo.module.point.repository.PointHistoryRepository;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static com.example.demo.module.point.entity.ReserveUse.*;

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
    private final PointHistoryRepository pointHistoryRepository;
    private final ReviewMyPageResultMapper reviewMyPageResultMapper;

    @Value("${point.review}")
    private Integer reviewCreatePoint;

    @Override
    @Transactional
    public Long save(Long userId, ReviewPayload reviewPayload) {

        //TODO : 이미지 저장 로직 추가 필요
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("해당하는 유저가 없습니다."));

        if (reviewRepository.existsByMedicineIdAndCreatedByUserId(reviewPayload.getMedicineId(), user.getUserId())) {
            throw new IllegalArgumentException("이미 후기를 작성한 영양제 입니다.");
        }
        Medicine medicine = medicineRepository.findById(reviewPayload.getMedicineId()).orElseThrow(() -> new NoSuchElementException("해당하는 영양제가 없습니다."));

        Review review = reviewRepository.save(
                Review.builder()
                        .title(reviewPayload.getTitle())
                        .content(reviewPayload.getContent())
                        .star(reviewPayload.getStar())
                        .heartCount(0)
                        .medicine(medicine)
                        .build());

        reviewPayload.getTagList().forEach(
                ht -> reviewHashtagRepository.save(
                        ReviewHashtag.builder()
                                .review(review)
                                .hashtag(hashtagRepository.findById(ht).orElseThrow())
                                .build()));

        pointHistoryRepository.save(
                PointHistory.builder()
                        .domain("review")
                        .changedValue(reviewCreatePoint)
                        .pointSum(user.reviewPoint(reviewCreatePoint))
                        .reserveUse(RESERVE).build());

        return review.getId();
    }

    @Override
    public ReviewResult findOneByReviewId(Long reviewId) {
        return reviewMapper.toDto(
                reviewRepository.findById(reviewId).orElseThrow(() -> new NoSuchElementException("리뷰가 존재하지 않습니다.")));
    }

    //이미지 수정은 따로 뺼 예정
    @Override
    @Transactional
    public Long editReview(Long userId, Long reviewId, ReviewEditPayload reviewEditPayload) {
        if (!reviewRepository.findById(reviewId).orElseThrow(() -> new NoSuchElementException("후기를 찾을 수 없습니다."))
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
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new NoSuchElementException("후기를 찾을 수 없습니다."));
        if (!review.getCreatedBy().getUserId().equals(userId)) {
            throw new IllegalArgumentException("후기 작성자만 삭제할 수 있습니다.");
        }
        reviewRepository.delete(review);
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("유저 정보를 찾을 수 없습니다."));

        // 포인트 삭감 메서드
        pointHistoryRepository.save(PointHistory.builder()
                .domain("review")
                .changedValue(reviewCreatePoint * (-1))
                .pointSum(user.cancelReviewPoint(reviewCreatePoint))
                .reserveUse(CANCELED).build());

        // 영양제 평점 구하는 메서드
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
}
