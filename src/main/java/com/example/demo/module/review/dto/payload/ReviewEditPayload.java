package com.example.demo.module.review.dto.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReviewEditPayload {
    @Schema(description = "리뷰 제목")
    @NotEmpty(message = "제목을 입력해주세요")
    private String title;

    @Schema(description = "hashtag PK 리스트")
    private List<Long> tagList = new ArrayList<>();

    @Schema(description = "리뷰 내용")
    @NotEmpty(message = "내용을 입력해주세요")
    private String content;

    @Schema(description = "별점 0~5 소수점 가능")
    @Range(min = 0, max = 5)
    private Double star;
}
