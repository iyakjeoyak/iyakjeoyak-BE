package com.example.demo.module.review.dto.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewMyPageResult {
    @Schema(description = "리뷰 PK")
    private Long id;

    @Schema(description = "영양제 간략 정보")
    private ReviewMedicineResult medicine;

    @Schema(description = "리뷰 제목")
    private String title;

    @Schema(description = "리뷰 내용")
    private String content;

    @Schema(description = "리뷰 별점")
    private Double star;

    @Schema(description = "리뷰 생성 일자")
    private LocalDateTime createdDate;

    @Schema(description = "리뷰 수정 일자")
    private LocalDateTime modifiedDate;
}