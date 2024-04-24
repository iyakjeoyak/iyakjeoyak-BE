package com.example.demo.module.review.dto.payload;

import lombok.Data;

@Data
public class ReviewImageDeletePayload {
    private Long reviewId;
    private Long imageId;
}
