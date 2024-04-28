package com.example.demo.module.review.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.module.hashtag.entity.Hashtag;
import com.example.demo.module.hashtag.repository.HashtagRepository;
import com.example.demo.module.image.entity.Image;
import com.example.demo.module.image.entity.ReviewImage;
import com.example.demo.module.image.repository.ReviewImageRepository;
import com.example.demo.module.image.service.ImageService;
import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.medicine.repository.MedicineRepository;
import com.example.demo.module.point.dto.result.PointHistoryResult;
import com.example.demo.module.point.service.PointHistoryService;
import com.example.demo.module.review.dto.payload.ReviewEditPayload;
import com.example.demo.module.review.dto.payload.ReviewPayload;
import com.example.demo.module.review.dto.result.ReviewResult;
import com.example.demo.module.review.entity.Review;
import com.example.demo.module.review.entity.ReviewHashtag;
import com.example.demo.module.review.repository.ReviewHashtagRepository;
import com.example.demo.module.review.repository.ReviewRepository;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.user.repository.UserRepository;
import com.example.demo.util.mapper.ReviewMapper;
import com.example.demo.util.mapper.ReviewMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demo.global.exception.ErrorCode.*;
import static com.example.demo.module.point.entity.PointDomain.REVIEW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MedicineRepository medicineRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private ReviewImageRepository reviewImageRepository;
    @Mock
    private ReviewHashtagRepository reviewHashtagRepository;
    @Mock
    private HashtagRepository hashtagRepository;
    @Mock
    private PointHistoryService pointHistoryService;
    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewMapperImpl reviewMapperImpl;


    @InjectMocks
    private ReviewServiceImpl reviewService;

    private User user;
    private Medicine medicine;
    private List<Image> imageList = new ArrayList<>();
    private Review review;
    private List<Hashtag> hashtagList = new ArrayList<>();
    private Double star;

    @BeforeEach
    void setUp() {
        star = 3.5;
        user = User.builder().userId(10L).point(100).username("testUser").build();
        medicine = Medicine.builder().id(20L).BSSH_NM("testMedicine").build();
        review = Review.builder().id(40L).title("testTitle").content("testContent").heartCount(0).star(star).medicine(medicine).createdBy(user).build();
        for (int i = 1; i <= 3; i++) {
            hashtagList.add(Hashtag.builder().id(1000L).name("testHash").build());
        }

        for (int i = 1; i <= 3; i++) {
            imageList.add(Image.builder().id(i * 100L).originName(String.valueOf(i)).build());
        }
    }

    //저장 관련 테스트 코드
    @Test
    @DisplayName("(성공) 리뷰 저장")
    public void save_success() throws Exception {
        //given
        ReviewPayload reviewPayload = setPayload();

        //when
        //1. 유저가 존재해야 함
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        //2. 이미 후기를 작성하지 않았어야함
        when(reviewRepository.existsByMedicineIdAndCreatedByUserId(medicine.getId(), user.getUserId())).thenReturn(false);
        //3.영양제가 존재해야 함
        when(medicineRepository.findById(medicine.getId())).thenReturn(Optional.of(medicine));
        //4. 리뷰 저장 정상 작동.
        when(reviewRepository.save(any())).thenReturn(review);
        //5. 이미지 저장 로직이 정상적으로 작동해야함
        when(imageService.saveImageList(any())).thenReturn(imageList);
        //6. 이미지 중간테이블 생성 성공
        when(reviewImageRepository.save(any(ReviewImage.class))).thenReturn(ReviewImage.builder().review(review).image(imageList.get(0)).build());
        //7. 해쉬태그가 있어야 한다.
        when(hashtagRepository.findById(any())).thenReturn(Optional.of(hashtagList.get(0)));
        //8. 해쉬태그 중간테이블 성공
        when(reviewHashtagRepository.save(any(ReviewHashtag.class))).thenReturn(ReviewHashtag.builder().review(review).hashtag(hashtagList.get(0)).build());
        //9. 포인트 히스토리 쌓임
        when(pointHistoryService.savePointHistory(any(), any(), any(), any(), any())).thenReturn(PointHistoryResult.builder().id(1L).domain(REVIEW).changedValue(10).pointSum(20).build());

        //then
        assertThat(reviewService.save(user.getUserId(), reviewPayload)).isEqualTo(review.getId());
    }

    @Test
    @DisplayName("(실패) 리뷰 저장 - 유저 정보가 잘못 됨")
    public void save_fail_userNotFound() throws Exception {
        //given
        ReviewPayload reviewPayload = setPayload();
        //when
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.ofNullable(null));

        //then
        assertThatThrownBy(() -> reviewService.save(user.getUserId(), reviewPayload)).isInstanceOf(CustomException.class).hasMessage(USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("(실패) 리뷰 저장 - 이미 리뷰 작성")
    public void save_fail_exist() throws Exception {
        //given
        ReviewPayload reviewPayload = setPayload();
        //when
        //1. 회원 정보가 있음
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        //2. 이미 후기를 작성해서 실패하는 케이스
        when(reviewRepository.existsByMedicineIdAndCreatedByUserId(medicine.getId(), user.getUserId())).thenReturn(true);

        //then
        assertThatThrownBy(() -> reviewService.save(user.getUserId(), reviewPayload)).isInstanceOf(CustomException.class).hasMessage(REVIEW_DUPLICATION.getMessage());
    }

    @Test
    @DisplayName("(실패) 리뷰 저장 - 영양제 정보가 잘못 됨")
    public void save_fail_medicineNotFound() throws Exception {
        //given
        ReviewPayload reviewPayload = setPayload();
        //when
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        //2. 이미 후기를 작성하지 않았어야함
        when(reviewRepository.existsByMedicineIdAndCreatedByUserId(medicine.getId(), user.getUserId())).thenReturn(false);
        //3.영양제가 존재해야 함
        when(medicineRepository.findById(medicine.getId())).thenReturn(Optional.ofNullable(null));

        //then
        assertThatThrownBy(() -> reviewService.save(user.getUserId(), reviewPayload)).isInstanceOf(CustomException.class).hasMessage(MEDICINE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("(실패) 리뷰 저장 - 이미지 저장 실패")
    public void save_fail_save_imageList() throws Exception {
        //given
        ReviewPayload reviewPayload = setPayload();

        //when
        //1. 유저가 존재해야 함
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        //2. 이미 후기를 작성하지 않았어야함
        when(reviewRepository.existsByMedicineIdAndCreatedByUserId(medicine.getId(), user.getUserId())).thenReturn(false);
        //3.영양제가 존재해야 함
        when(medicineRepository.findById(medicine.getId())).thenReturn(Optional.of(medicine));
        //4. 리뷰 저장 정상 작동.
        when(reviewRepository.save(any())).thenReturn(review);
        //5. 이미지 저장 로직이 정상적으로 작동해야함
        when(imageService.saveImageList(any())).thenThrow(new IOException());

        assertThatThrownBy(() -> reviewService.save(user.getUserId(), reviewPayload)).isInstanceOf(IOException.class);
    }

    @Test
    @DisplayName("(실패) 리뷰 저장 - 해쉬태그 정보 없음")
    public void save_fail_hashtagNotFound() throws Exception {
        //given
        ReviewPayload reviewPayload = setPayload();

        //when
        //1. 유저가 존재해야 함
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        //2. 이미 후기를 작성하지 않았어야함
        when(reviewRepository.existsByMedicineIdAndCreatedByUserId(medicine.getId(), user.getUserId())).thenReturn(false);
        //3.영양제가 존재해야 함
        when(medicineRepository.findById(medicine.getId())).thenReturn(Optional.of(medicine));
        //4. 리뷰 저장 정상 작동.
        when(reviewRepository.save(any())).thenReturn(review);
        //5. 이미지 저장 로직이 정상적으로 작동해야함
        when(imageService.saveImageList(any())).thenReturn(imageList);
        //6. 이미지 중간테이블 생성 성공
        when(reviewImageRepository.save(any(ReviewImage.class))).thenReturn(ReviewImage.builder().review(review).image(imageList.get(0)).build());
        //7. 해쉬태그가 있어야 한다.
        when(hashtagRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        //then
        assertThatThrownBy(() -> reviewService.save(user.getUserId(), reviewPayload)).isInstanceOf(CustomException.class).hasMessage(HASHTAG_NOT_FOUND.getMessage());
    }

    private ReviewPayload setPayload() {
        ReviewPayload reviewPayload = new ReviewPayload();
        reviewPayload.setTagList(hashtagList.stream().map(Hashtag::getId).toList());
        reviewPayload.setImgList(List.of());
        reviewPayload.setStar(star);
        reviewPayload.setMedicineId(medicine.getId());
        reviewPayload.setTitle("testTitle");
        return reviewPayload;
    }

    //단일 조회 테스트
    @Test
    @DisplayName("(성공) 리뷰 조회 - 성공")
    public void findOne_success() throws Exception {
        //given
        ReviewResult given = reviewMapperImpl.toDto(review);
        //when
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        when(reviewMapper.toDto(review)).thenReturn(given);
        //then
        ReviewResult result = reviewService.findOneByReviewId(review.getId());
        assertThat(result.getId()).isEqualTo(review.getId());
        assertThat(result.getTitle()).isEqualTo(review.getTitle());
        assertThat(result.getContent()).isEqualTo(review.getContent());
        assertThat(result.getStar()).isEqualTo(review.getStar());
        assertThat(result.getHeartCount()).isEqualTo(review.getHeartCount());
    }
    @Test
    @DisplayName("(실패) 리뷰 조회 - 리뷰 정보 없음")
    public void findOne_reviewNotFound() throws Exception {
        //given

        //when
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.ofNullable(null));
        //then
        assertThatThrownBy(() -> reviewService.findOneByReviewId(review.getId())).isInstanceOf(CustomException.class).hasMessage(REVIEW_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("(성공) 리뷰 수정")
    public void edit_success() throws Exception {
        //given
        Long originId = review.getId();
        Double editStar = 0.5;
        String editTitle = "수정 타이틀";
        String editContent = "수정 내용";
        List<Long> hashtagIdList = List.of(1L, 2L);

        ReviewEditPayload reviewEditPayload = setReviewEditPayload(editStar, editTitle, editContent , hashtagIdList);
        ReviewHashtag reviewHashtag = ReviewHashtag.builder().review(review).hashtag(hashtagList.get(0)).build();
        Hashtag hashtag = Hashtag.builder().id(10L).name("테스트 해쉬태그").build();

        //when
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        when(reviewHashtagRepository.findAllByReviewId(review.getId())).thenReturn(List.of(ReviewHashtag.builder().id(100L).hashtag(hashtagList.get(0)).review(review).build()));
        when(reviewHashtagRepository.existsByReviewIdAndHashtagId(review.getId(), reviewEditPayload.getTagList().get(0))).thenReturn(false);
        when(reviewHashtagRepository.save(any(ReviewHashtag.class))).thenReturn(reviewHashtag);
        when(hashtagRepository.findById(any())).thenReturn(Optional.of(hashtag));

        reviewService.editReview(user.getUserId(), review.getId(), reviewEditPayload);
        //then
        assertThat(review.getId()).isEqualTo(originId);
        assertThat(review.getTitle()).isEqualTo(editTitle);
        assertThat(review.getContent()).isEqualTo(editContent);
        assertThat(review.getStar()).isEqualTo(editStar);
    }
    @Test
    @DisplayName("(실패) 리뷰 수정 - 리뷰가 없음")
    public void edit_fail_reviewNotFound() throws Exception {
        //given
        Double editStar = 0.5;
        String editTitle = "수정 타이틀";
        String editContent = "수정 내용";
        List<Long> hashtagIdList = List.of(1L, 2L);
        ReviewEditPayload reviewEditPayload = setReviewEditPayload(editStar, editTitle, editContent, hashtagIdList);
        //when
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.ofNullable(null));

        //then
        assertThatThrownBy(() -> reviewService.editReview(user.getUserId(), review.getId(), reviewEditPayload)).isInstanceOf(CustomException.class).hasMessage(REVIEW_NOT_FOUND.getMessage());
    }

    private static ReviewEditPayload setReviewEditPayload(Double editStar, String editTitle, String editContent, List<Long> hashtagIdList) {
        ReviewEditPayload reviewEditPayload = new ReviewEditPayload();
        reviewEditPayload.setStar(editStar);
        reviewEditPayload.setTitle(editTitle);
        reviewEditPayload.setContent(editContent);
        reviewEditPayload.setTagList(hashtagIdList);
        return reviewEditPayload;
    }


}