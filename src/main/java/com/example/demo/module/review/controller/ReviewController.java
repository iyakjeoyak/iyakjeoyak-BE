package com.example.demo.module.review.controller;


import com.example.demo.module.review.service.ReviewService;
import com.example.demo.module.review.dto.payload.ReviewEditPayload;
import com.example.demo.module.review.dto.payload.ReviewPayload;
import com.example.demo.module.review.dto.result.ReviewResult;
import com.example.demo.module.common.result.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "(후기)", description = "후기 관련")
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping()
    @Operation(summary = "영양제 리뷰 전체 조회", description = "page : 현재 페이지 , size : 페이지당 데이터 수 , medicineId : 영양제 PK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PageResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<PageResult<ReviewResult>> findAllByPage(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size, @RequestParam(name = "medicineId") Long medicineId) {
        return new ResponseEntity<>(reviewService.findPageByMedicineId(medicineId, PageRequest.of(page, size)), HttpStatus.OK);
    }

    @GetMapping("/{reviewId}")
    @Operation(summary = "리뷰 단건 조회", description = "리뷰 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ReviewResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<ReviewResult> findOneByMedicineId(@PathVariable("reviewId") Long reviewId) {
        return new ResponseEntity<>(reviewService.findOneByReviewId(reviewId), HttpStatus.OK);
    }

    @PostMapping("")
    @Operation(summary = "리뷰 생성", description = "리부 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> insertReview(@RequestBody ReviewPayload reviewPayload , @RequestParam("userId")Long userId) {
        return new ResponseEntity<>(reviewService.save(userId,reviewPayload), HttpStatus.CREATED);
    }

    @PatchMapping("/{reviewId}")
    @Operation(summary = "리뷰 수정", description = "리뷰 수정 (이미지 제외)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> editReview(@PathVariable("reviewId") Long reviewId, @RequestBody ReviewEditPayload editPayload) {
        return new ResponseEntity<>(reviewService.editReview(reviewId, editPayload), HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제", description = "리뷰 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> deleteReview(@PathVariable("reviewId") Long reviewId) {
        return new ResponseEntity<>(reviewService.deleteByReviewId(reviewId), HttpStatus.OK);
    }


}
