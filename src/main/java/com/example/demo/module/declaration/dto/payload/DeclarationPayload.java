package com.example.demo.module.declaration.dto.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeclarationPayload {
    @Schema(description = "신고 제목")
    @NotEmpty(message = "신고 제목을 입력해주세요")
    private String title;

    @Schema(description = "신고 내용")
    @NotEmpty(message = "신고 내용을 입력해주세요")
    private String content;

    @Schema(description = "리뷰 PK")
    @NotNull
    private Long reviewId;
}
