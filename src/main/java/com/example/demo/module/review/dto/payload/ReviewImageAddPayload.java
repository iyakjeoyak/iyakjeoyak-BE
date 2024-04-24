package com.example.demo.module.review.dto.payload;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ReviewImageAddPayload {

    private Long reviewId;
    private List<MultipartFile> images;
}
