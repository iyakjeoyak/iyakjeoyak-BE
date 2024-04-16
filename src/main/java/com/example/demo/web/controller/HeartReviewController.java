package com.example.demo.web.controller;

import com.example.demo.service.HeartReviewService;
import com.example.demo.web.payload.HeartReviewPayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "(좋아요)", description = "후기 좋아요 관련")
@RequestMapping("/heart/review")
public class HeartReviewController {
    private final HeartReviewService heartReviewService;

    //ToDO : 시큐리티 Authentication 적용 후 User 식별 변경 해야함

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
    public ResponseEntity<Boolean> checkHeartReview(@PathVariable("reviewId") Long reviewId, @RequestParam("userId") Long userId) {
        return new ResponseEntity<>(heartReviewService.checkReviewHeart(userId, reviewId), HttpStatus.OK);
    }

    @PostMapping("")
    @Operation(summary = "영양제 리뷰 좋아요", description = "바디에 review PK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> saveHeartReview(@RequestBody HeartReviewPayload payload) {
        return new ResponseEntity<>(heartReviewService.save(payload.getUserId(), payload.getReviewId()), HttpStatus.OK);
    }
    @DeleteMapping("/{reviewId}")
    @Operation(summary = "영양제 리뷰 좋아요 삭제", description = "쿼리파라미터로 review PK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> deleteHeartReview(@PathVariable("reviewId") Long reviewId, @RequestParam("userId") Long userId) {
        return new ResponseEntity<>(heartReviewService.delete(userId, reviewId), HttpStatus.OK);
    }
}
