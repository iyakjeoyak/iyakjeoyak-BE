package com.example.demo.module.user.controller;


import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.review.dto.payload.ReviewOrderField;
import com.example.demo.module.review.dto.result.ReviewMyPageResult;
import com.example.demo.module.review.dto.result.ReviewSimpleMyPageResult;
import com.example.demo.module.review.service.ReviewService;
import com.example.demo.module.user.dto.result.UserDetailResult;
import com.example.demo.module.user.dto.result.UserResult;
import com.example.demo.module.user.service.UserService;
import com.example.demo.module.userStorage.dto.payload.StorageOrderField;
import com.example.demo.module.userStorage.dto.result.UserStorageSimpleResult;
import com.example.demo.module.userStorage.service.UserStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.demo.module.review.dto.payload.ReviewOrderField.CREATED_DATE;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "(마이페이지)", description = "마이페이지 API")
@RequestMapping("/my")
public class MyPageController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final UserStorageService userStorageService;

    @GetMapping("/user")
    @Operation(summary = "유저 단건 조회", description = "유저 단건 조회")
    public ResponseEntity<UserDetailResult> findOneByUserId(@AuthenticationPrincipal Long userId) {
        UserResult userResult = userService.findOneByUserId(userId);
        List<ReviewSimpleMyPageResult> simpleResults = reviewService.findSimpleResultPageByUserId(userId, PageRequest.of(0, 3, Sort.Direction.DESC, CREATED_DATE.getValue()));
        List<UserStorageSimpleResult> userStorageList = userStorageService.getAllByUserId(userId, PageRequest.of(0, 4, Sort.Direction.DESC, StorageOrderField.CREATED_DATE.getValue())).getData();
        UserDetailResult userDetailResult = getUserDetailResult(userResult, simpleResults, userStorageList);
        return new ResponseEntity<>(userDetailResult, HttpStatus.OK);
    }

    @GetMapping("/review")
    @Operation(summary = "마이페이지 리뷰 전체 조회", description = "page : 현재 페이지 , size : 페이지당 데이터 수")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PageResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<PageResult<ReviewMyPageResult>> findPageByUserId(
            @AuthenticationPrincipal Long userId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "5", required = false) int size,
            @RequestParam(name = "orderBy", defaultValue = "ID", required = false) ReviewOrderField reviewOrderField,
            @RequestParam(name = "sort", defaultValue = "DESC", required = false) String sort) {
        Sort orderBy = sort.equals("ASC") ?
                Sort.by(Sort.Direction.ASC, reviewOrderField.getValue()) : Sort.by(Sort.Direction.DESC, reviewOrderField.getValue());
        return new ResponseEntity<>(reviewService.findPageByUserId(userId, PageRequest.of(page, size, orderBy)), HttpStatus.OK);
    }

    private UserDetailResult getUserDetailResult(UserResult userResult, List<ReviewSimpleMyPageResult> reviewList, List<UserStorageSimpleResult> userStorageList) {
        UserDetailResult userDetailResult = new UserDetailResult();
        userDetailResult.setUserResult(userResult);
        userDetailResult.setLatestReviews(reviewList);
        userDetailResult.setFavoriteSupplements(userStorageList);
        return userDetailResult;
    }
}
