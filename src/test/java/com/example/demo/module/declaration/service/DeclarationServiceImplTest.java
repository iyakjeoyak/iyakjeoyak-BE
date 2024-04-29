//package com.example.demo.module.declaration.service;
//
//import com.example.demo.module.common.result.PageResult;
//import com.example.demo.module.declaration.dto.payload.DeclarationPayload;
//import com.example.demo.module.declaration.dto.result.DeclarationResult;
//import com.example.demo.module.declaration.entity.Declaration;
//import com.example.demo.module.declaration.repository.DeclarationRepository;
//import com.example.demo.module.review.entity.Review;
//import com.example.demo.module.review.repository.ReviewRepository;
//import com.example.demo.module.user.entity.User;
//import com.example.demo.module.user.repository.UserRepository;
//import com.example.demo.util.mapper.ReviewMapper;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.when;
//@ExtendWith(MockitoExtension.class)
//class DeclarationServiceImplTest {
//    @Mock
//    private DeclarationRepository declarationRepository;
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private ReviewRepository reviewRepository;
//    @Mock
//    private ReviewMapper reviewMapper;
//    @InjectMocks
//    private DeclarationServiceImpl declarationService;
//    DeclarationPayload declarationPayload;
//    User user;
//    @BeforeEach
//    void setup(){
//        declarationPayload = new DeclarationPayload();
//        declarationPayload.setTitle("신고제목");
//        declarationPayload.setContent("신고내용");
//        user = User.builder().userId(1L).build();
//    }
//    @Test
//    void findAll(){
//        Review review1 = Review.builder().build();
//        Review review2 = Review.builder().build();
//        Review review3 = Review.builder().build();
//        Declaration declaration1 = Declaration.builder().title("신고1").content("신고함1").user(user).review(review1).build();
//        Declaration declaration2 = Declaration.builder().title("신고2").content("신고함2").user(user).review(review2).build();
//        Declaration declaration3 = Declaration.builder().title("신고3").content("신고함3").user(user).review(review3).build();
//
//        PageRequest pageRequest = PageRequest.of(0, 3);
//        Page<Declaration> declarationPage = new PageImpl<>(Arrays.asList(declaration1, declaration2, declaration3));
//
//        when(declarationRepository.findAllByUserUserId(user.getUserId(), pageRequest)).thenReturn(declarationPage);
//        Page<DeclarationResult> result = declarationRepository.findAllByUserUserId(user.getUserId(), pageRequest).map(declaration -> declaration.toDto(reviewMapper));
//        PageResult<DeclarationResult> declarationResult = new PageResult<>(result);
//
//        assertEquals(3, declarationResult.getTotalElement());
//        assertEquals(1, declarationResult.getTotalPages());
//        for(int i=0;i<3;i++){
//            assertEquals("신고"+(i+1), declarationResult.getData().get(i).getTitle());
//            assertEquals("신고함"+(i+1), declarationResult.getData().get(i).getContent());
//        }
//    }
//    @Test
//    void findOneByUser(){
//        Declaration declaration = Declaration.builder()
//                .id(1L).user(user).title(declarationPayload.getTitle()).content(declarationPayload.getContent()).build();
//        DeclarationResult declarationResult = declaration.toDto(reviewMapper);
//        when(declarationRepository.existsByIdAndUserUserId(1L, user.getUserId())).thenReturn(true);
//        when(declarationRepository.findById(anyLong())).thenReturn(Optional.of(declaration));
//
//        DeclarationResult result = declarationService.findOneByUser(1L, user.getUserId());
//
//        assertEquals(declarationResult, result);
//    }
//    @Test
//    void save(){
//        Review review = Review.builder().build();
//        when(reviewRepository.findById(declarationPayload.getReviewId())).thenReturn(Optional.ofNullable(review));
//        when(userRepository.findById(user.getUserId())).thenReturn(Optional.ofNullable(user));
//        when(declarationRepository.existsByReviewIdAndUserUserId(declarationPayload.getReviewId(), user.getUserId())).thenReturn(false);
//        Declaration declaration = Declaration.builder()
//                .id(1L).user(user).review(review).title(declarationPayload.getTitle()).content(declarationPayload.getContent()).build();
//        when(declarationRepository.save(any(Declaration.class))).thenReturn(declaration);
//
//        Long id = declarationService.save(declarationPayload, user.getUserId());
//
//        assertEquals(declaration.getId(), id);
//    }
//    @Test
//    void delete(){
//        when(declarationRepository.existsByIdAndUserUserId(1L, user.getUserId())).thenReturn(true);
//
//        Long id = declarationService.delete(1L, user.getUserId());
//
//        assertEquals(1L, id);
//    }
//}