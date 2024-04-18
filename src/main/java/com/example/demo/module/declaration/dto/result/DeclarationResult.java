package com.example.demo.module.declaration.dto.result;

import com.example.demo.module.review.entity.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DeclarationResult {
    @Schema(description = "신고 PK")
    private Long id;

    @Schema(description = "신고 제목")
    private String title;

    @Schema(description = "신고 내용")
    private String content;

    @Schema(description = "신고한 리뷰")
    private Long reviewId;
}
