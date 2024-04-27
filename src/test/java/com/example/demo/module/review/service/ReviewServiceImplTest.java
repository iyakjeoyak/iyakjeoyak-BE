package com.example.demo.module.review.service;

import com.example.demo.module.hashtag.entity.Hashtag;
import com.example.demo.module.hashtag.repository.HashtagRepository;
import com.example.demo.module.image.entity.Image;
import com.example.demo.module.image.entity.ReviewImage;
import com.example.demo.module.image.repository.ReviewImageRepository;
import com.example.demo.module.image.service.ImageService;
import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.medicine.repository.MedicineRepository;
import com.example.demo.module.point.dto.result.PointHistoryResult;
import com.example.demo.module.point.entity.PointDomain;
import com.example.demo.module.point.entity.ReserveUse;
import com.example.demo.module.point.service.PointHistoryService;
import com.example.demo.module.review.dto.payload.ReviewPayload;
import com.example.demo.module.review.entity.Review;
import com.example.demo.module.review.entity.ReviewHashtag;
import com.example.demo.module.review.repository.ReviewHashtagRepository;
import com.example.demo.module.review.repository.ReviewRepository;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demo.module.point.entity.PointDomain.REVIEW;
import static com.example.demo.module.point.entity.ReserveUse.RESERVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
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


    @InjectMocks
    private ReviewServiceImpl reviewService;

    private User user;
    private Medicine medicine;
    private List<Image> imageList = new ArrayList<>();
    private Review review;
    private List<Hashtag> hashtagList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        user = User.builder().userId(10L).point(100).username("testUser").build();
        medicine = Medicine.builder().id(20L).BSSH_NM("testMedicine").build();
        review = Review.builder().id(40L).title("testTitle").heartCount(0).build();
        for (int i = 1; i <= 3; i++) {
            hashtagList.add(Hashtag.builder().id(1000L).name("testHash").build());
        }

        for (int i = 1; i <= 3; i++) {
            imageList.add(Image.builder().id(i * 100L).originName(String.valueOf(i)).build());
        }
        reviewService.setReviewCreatePoint(10);
    }
    
    @Test
    @DisplayName("(성공) 리뷰 저장")
    public void save_success() throws Exception {
        //given
        ReviewPayload reviewPayload = new ReviewPayload();
        reviewPayload.setTagList(hashtagList.stream().map(Hashtag::getId).toList());
        reviewPayload.setImgList(List.of());
        reviewPayload.setStar(3.5);
        reviewPayload.setMedicineId(medicine.getId());
        reviewPayload.setTitle("testTitle");
        //when
        //1. 유저가 존재해야 함
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        //2. 이미 후기를 작성하지 않았어야함
        when(reviewRepository.existsByMedicineIdAndCreatedByUserId(medicine.getId(), user.getUserId())).thenReturn(false);
        //3.영양제가 존재해야 함
        when(medicineRepository.findById(medicine.getId())).thenReturn(Optional.of(medicine));

        when(reviewRepository.save(any())).thenReturn(review);

        //4. 이미지 저장 로직이 정상적으로 작동해야함
        when(imageService.saveImageList(any())).thenReturn(imageList);
        //5. 이미지 중간테이블 생성 성공
        when(reviewImageRepository.save(any(ReviewImage.class))).thenReturn(ReviewImage.builder().review(review).image(imageList.get(0)).build());
        //6. 해쉬태그가 있어야 한다.
        when(hashtagRepository.findById(any())).thenReturn(Optional.of(hashtagList.get(0)));
        //7. 해쉬태그 중간테이블 성공
        when(reviewHashtagRepository.save(any(ReviewHashtag.class))).thenReturn(ReviewHashtag.builder().review(review).hashtag(hashtagList.get(0)).build());

        when(pointHistoryService.savePointHistory(any(), any(), any(), any(), any())).thenReturn(PointHistoryResult.builder().id(1L).domain(REVIEW).changedValue(10).pointSum(20).build());

        //then
        assertThat(reviewService.save(user.getUserId(), reviewPayload)).isEqualTo(review.getId());
    }

}