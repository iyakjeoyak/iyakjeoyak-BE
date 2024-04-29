package com.example.demo.module.review.dto.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResult {

    @Schema(description = "후기 PK")
    private Long id;

    @Schema(description = "후기 제목")
    private String title;

    @Schema(description = "후기 내용")
    private String content;

    @Schema(description = "후기 별점")
    private Double star;

    @Schema(description = "후기 좋아요 수")
    private Integer heartCount;
}
