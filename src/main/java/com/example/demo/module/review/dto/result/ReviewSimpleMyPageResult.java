package com.example.demo.module.review.dto.result;

import com.example.demo.module.review.entity.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewSimpleMyPageResult {
    @Schema(description = "리뷰 PK")
    private Long id;

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

    public static ReviewSimpleMyPageResult toDto(Review review) {
        return ReviewSimpleMyPageResult.builder()
                .id(review.getId())
                .title(review.getTitle())
                .content(review.getContent())
                .star(review.getStar())
                .createdDate(review.getCreatedDate())
                .modifiedDate(review.getModifiedDate())
                .build();
    }
}
