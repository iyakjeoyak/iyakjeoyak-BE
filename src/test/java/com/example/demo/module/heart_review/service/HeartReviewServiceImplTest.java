package com.example.demo.module.heart_review.service;

import com.example.demo.module.heart_review.entity.HeartReview;
import com.example.demo.module.heart_review.repository.HeartReviewRepository;
import com.example.demo.module.review.entity.Review;
import com.example.demo.module.review.repository.ReviewRepository;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HeartReviewServiceImplTest {
    @Mock
    private HeartReviewRepository heartReviewRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private HeartReviewServiceImpl heartReviewService;

    private Long heartReviewId;
    private User user;
    private Review review;

    @BeforeEach
    void setUp() {
        heartReviewId = 1000L;

        user = User.builder().userId(2000L).username("testUsername").build();
        review = Review.builder().id(3000L).title("testTitle").heartCount(0).build();

    }

    @Test
    @DisplayName("(성공) : 좋아요")
        public void SaveSuccess() throws Exception {
        //given : beforeEach 상황

        //when
        // 1. 좋아요 누르지 않은 상황
        when(heartReviewRepository.existsByUserUserIdAndReviewId(user.getUserId(), review.getId())).thenReturn(false);

        //2. 리뷰Id로 찾으면 위에 만든 리뷰가 나와야 함
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.ofNullable(review));

        //3. userId 찾으면 위에 만든 유저가 나와야 함
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.ofNullable(user));

        //4. 좋아요 save 했을때 상황
        when(heartReviewRepository.save(any(HeartReview.class))).thenReturn(HeartReview.builder().id(heartReviewId).user(user).review(review).build());

        Long save = heartReviewService.save(user.getUserId(), review.getId());

        //then
        assertThat(save).isEqualTo(heartReviewId);
    }
    @Test
    @DisplayName("(실패) : 좋아요 이미 눌른 상태에서 다시 좋아요")
    public void SaveFailAtExist() throws Exception {
        //given : beforeEach 상황
        HeartReview heart = HeartReview.builder().id(heartReviewId).review(review).user(user).build();

        //when
        // 1. 좋아요 이미 누른 상황
        when(heartReviewRepository.existsByUserUserIdAndReviewId(heart.getUser().getUserId(), heart.getReview().getId())).thenReturn(true);

        //then
        assertThatThrownBy(() -> heartReviewService.save(user.getUserId(), review.getId())).isInstanceOf(IllegalArgumentException.class).hasMessage("이미 좋아요 클릭한 후기입니다.");
    }

    @Test
    @DisplayName("(성공) : 좋아요 취소")
    public void deleteSuccess() throws Exception {
        //given : beforeEach 상황
        HeartReview heart = HeartReview.builder().id(heartReviewId).review(review).user(user).build();
        review.addHeartCount();

        //when
        // 1. 좋아요 누른 상황
        when(heartReviewRepository.existsByUserUserIdAndReviewId(user.getUserId(), review.getId())).thenReturn(true);

        //2. 리뷰Id로 찾으면 위에 만든 리뷰가 나와야 함
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.ofNullable(review));

        //3. userId 가 누른 Heart 찾는 상황
        when(heartReviewRepository.findByUserUserIdAndReviewId(user.getUserId(), review.getId())).thenReturn(Optional.ofNullable(heart));

        //4. 좋아요 save 했을때 상황

        Long delete = heartReviewService.delete(user.getUserId(), review.getId());

        //then
        assertThat(delete).isEqualTo(heartReviewId);
    }
    @Test
    @DisplayName("(실패) : 좋아요 누르지 않고 삭제")
    public void deleteFailNotExist() throws Exception {
        //given : beforeEach 상황

        //when
        // 1. 좋아요 누르지 않은 상황
        when(heartReviewRepository.existsByUserUserIdAndReviewId(user.getUserId(), review.getId())).thenReturn(false);

        //then
        assertThatThrownBy(() -> heartReviewService.delete(user.getUserId(), review.getId())).isInstanceOf(IllegalArgumentException.class).hasMessage("좋아요 클릭 되지 않은 후기입니다.");
    }

    @Test
    @DisplayName("(실패) : 좋아요 누를 때 리뷰가 없음")
    public void saveFailReviewNotFound() throws Exception {
        //given : beforeEach 상황

        //when
        // 1. 좋아요 누르지 않은 상황
        when(heartReviewRepository.existsByUserUserIdAndReviewId(user.getUserId(), review.getId())).thenReturn(false);

        //2. 리뷰Id로 찾으면 위에 만든 리뷰가 나와야 함
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.ofNullable(null));

        //then
        assertThatThrownBy(() -> heartReviewService.save(user.getUserId(), review.getId())).isInstanceOf(NoSuchElementException.class).hasMessage("후기를 찾을 수 없습니다.");
    }
    @Test
    @DisplayName("(실패) : 유저정보가 잘못됨")
    public void saveFailUserNotFound() throws Exception {
        //given : beforeEach 상황

        //when
        // 1. 좋아요 누르지 않은 상황
        when(heartReviewRepository.existsByUserUserIdAndReviewId(user.getUserId(), review.getId())).thenReturn(false);

        //2. 리뷰Id로 찾으면 위에 만든 리뷰가 나와야 함
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.ofNullable(review));

        //3. userId 찾으면 위에 만든 유저가 나와야 함
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.ofNullable(null));

        //then
        assertThatThrownBy(() -> heartReviewService.save(user.getUserId(), review.getId())).isInstanceOf(NoSuchElementException.class).hasMessage("유저 정보를 찾지 못했습니다.");
    }

    @Test
    @DisplayName("(성공) : 좋아요 여부 확인 테스트")
    public void checkHeart() throws Exception {
        //given : 이미 좋아요 누른 상태
        HeartReview heart = HeartReview.builder().id(heartReviewId).review(review).user(user).build();
        //when
        when(heartReviewRepository.existsByUserUserIdAndReviewId(heart.getUser().getUserId(), heart.getReview().getId())).thenReturn(true);
        //then
        Boolean result = heartReviewService.checkReviewHeart(user.getUserId(), review.getId());

        assertThat(result).isEqualTo(true);
    }
}