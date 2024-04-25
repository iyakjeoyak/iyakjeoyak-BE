package com.example.demo.module.heart_review.dto.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HeartReviewPayload {
    @Schema(description = "리뷰 PK")
    private Long reviewId;
}
