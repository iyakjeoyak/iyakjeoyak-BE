package com.example.demo.module.heart_review.controller;

import com.example.demo.module.heart_review.dto.payload.HeartReviewPayload;
import com.example.demo.module.heart_review.service.HeartReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "(후기-좋아요)", description = "후기 좋아요 관련")
@RequestMapping("/heart/review")
public class HeartReviewController {
    private final HeartReviewService heartReviewService;


    @GetMapping("/count/{reviewId}")
    @Operation(summary = "영양제 리뷰 좋아요 수 조회", description = "특정 리뷰의 좋아요 수")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Integer> getCountByReviewId(@PathVariable("reviewId") Long reviewId) {
        return new ResponseEntity<>(heartReviewService.getHeartCountByReviewId(reviewId), HttpStatus.OK);
    }

    @GetMapping("/{reviewId}")
    @Operation(summary = "영양제 리뷰 좋아요 여부 확인", description = "이미 눌렀으면 true")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Boolean> checkHeartReview(@PathVariable("reviewId") Long reviewId, @AuthenticationPrincipal Long userId) {
        return new ResponseEntity<>(heartReviewService.checkReviewHeart(userId, reviewId), HttpStatus.OK);
    }

    @PostMapping("")
    @Operation(summary = "영양제 리뷰 좋아요", description = "바디에 review PK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> saveHeartReview(@RequestBody HeartReviewPayload payload, @AuthenticationPrincipal Long userId) {
        return new ResponseEntity<>(heartReviewService.save(userId, payload.getReviewId()), HttpStatus.OK);
    }
    @DeleteMapping("/{reviewId}")
    @Operation(summary = "영양제 리뷰 좋아요 삭제", description = "쿼리파라미터로 review PK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> deleteHeartReview(@PathVariable("reviewId") Long reviewId, @AuthenticationPrincipal Long userId) {
        return new ResponseEntity<>(heartReviewService.delete(userId, reviewId), HttpStatus.OK);
    }

    @PostMapping("/click")
    @Operation(summary = "리뷰 좋아요 생성/삭제", description = "리뷰 좋아요 클릭하면 true / 취소하면 false ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "좋아요 생성", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "200", description = "좋아요 삭제", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Boolean> clickHeartReview(@RequestBody HeartReviewPayload payload, @AuthenticationPrincipal Long userId){
        if (heartReviewService.click(payload.getReviewId(), userId)){
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        } return new ResponseEntity<>(false, HttpStatus.OK);
    }
}
