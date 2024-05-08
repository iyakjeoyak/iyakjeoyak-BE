package com.example.demo.module.review.controller;


import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.review.dto.payload.ReviewEditPayload;
import com.example.demo.module.review.dto.payload.ReviewImageAddPayload;
import com.example.demo.module.review.dto.payload.ReviewOrderField;
import com.example.demo.module.review.dto.payload.ReviewPayload;
import com.example.demo.module.review.dto.result.ReviewDetailResult;
import com.example.demo.module.review.dto.result.ReviewMyPageResult;
import com.example.demo.module.review.dto.result.ReviewResult;
import com.example.demo.module.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    public ResponseEntity<PageResult<ReviewResult>> findAllByPage(
            @RequestParam(name = "medicineId") Long medicineId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "orderBy", defaultValue = "ID", required = false) ReviewOrderField reviewOrderField,
            @RequestParam(name = "sort", defaultValue = "DESC", required = false) String sort,
            @AuthenticationPrincipal Long userId) {
        Sort orderBy = sort.equals("ASC") ?
                Sort.by(Sort.Direction.ASC, reviewOrderField.getValue()) : Sort.by(Sort.Direction.DESC, reviewOrderField.getValue());
        PageResult<ReviewResult> pageResult = reviewService.findPageByMedicineId(medicineId, PageRequest.of(page, size, orderBy));
        pageResult.getData().forEach(r -> r.setIsOwner(r.getCreatedBy().getUserId().equals(userId)));
        return ResponseEntity.status(HttpStatus.OK).body(pageResult);
    }

    @GetMapping("/{reviewId}")
    @Operation(summary = "리뷰 단건 조회", description = "리뷰 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ReviewDetailResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<ReviewDetailResult> findOneByMedicineId(@AuthenticationPrincipal Long userId , @PathVariable("reviewId") Long reviewId) {
        ReviewDetailResult oneByReviewId = reviewService.findOneByReviewId(reviewId);
        oneByReviewId.setIsOwner(oneByReviewId.getCreatedBy().getUserId().equals(userId));
        return new ResponseEntity<>(oneByReviewId, HttpStatus.OK);
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "리뷰 생성", description = "리뷰 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> insertReview(@RequestPart(name = "reviewPayload") ReviewPayload reviewPayload, @RequestPart(name = "imgFile", required = false) List<MultipartFile> imgFile, @AuthenticationPrincipal Long userId) throws IOException {
        return new ResponseEntity<>(reviewService.save(userId, reviewPayload, imgFile), HttpStatus.CREATED);
    }

    @PatchMapping("/{reviewId}")
    @Operation(summary = "리뷰 수정", description = "리뷰 수정 (이미지 제외)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> editReview(@PathVariable("reviewId") Long reviewId, @RequestBody ReviewEditPayload editPayload, @AuthenticationPrincipal Long userId) {
        return new ResponseEntity<>(reviewService.editReview(userId, reviewId, editPayload), HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제", description = "리뷰 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> deleteReview(@PathVariable("reviewId") Long reviewId, @AuthenticationPrincipal Long userId) {
        System.out.println("ReviewController.deleteReview");
        return new ResponseEntity<>(reviewService.deleteByReviewId(userId, reviewId), HttpStatus.OK);
    }


    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "리뷰 이미지 추가(복수)", description = "반환값 : 리뷰 PK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> addReviewImages(@RequestParam("reviewId") Long reviewId, @RequestPart(name = "imgFile", required = false) List<MultipartFile> imgFile, @AuthenticationPrincipal Long userId) throws IOException {
        ReviewImageAddPayload payload = new ReviewImageAddPayload();
        payload.setReviewId(reviewId);
        payload.setImages(imgFile);
        return new ResponseEntity<>(reviewService.addReviewImage(userId, payload.getReviewId(), payload.getImages()), HttpStatus.CREATED);
    }

    @DeleteMapping("/image")
    @Operation(summary = "리뷰 이미지 삭제", description = "반환값 : 리뷰 PK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> deleteReviewImage(@RequestParam("reviewId") Long reviewId, @RequestParam("imageId") Long imageId, @AuthenticationPrincipal Long userId) throws IOException {
        return new ResponseEntity<>(reviewService.deleteReviewImage(userId, reviewId, imageId), HttpStatus.CREATED);
    }

    @GetMapping("/top")
    @Operation(summary = "베스트 리뷰 조회", description = "리뷰 좋아요 개수 기준")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<List<ReviewDetailResult>> findTopReview(@RequestParam(name = "size", defaultValue = "5", required = false) int size) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.findTopReview(size));
    }
}
