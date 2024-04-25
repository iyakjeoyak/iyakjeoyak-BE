package com.example.demo.module.review.dto.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ReviewImageAddPayload {

    @Schema(description = "리뷰 PK")
    private Long reviewId;

    @Schema(description = "추가할 이미지 리스트")
    private List<MultipartFile> images;
}
