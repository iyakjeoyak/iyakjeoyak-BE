package com.example.demo.module.review.dto.result;

import com.example.demo.module.hashtag.dto.result.HashtagResult;
import com.example.demo.module.image.dto.result.ImageResult;
import com.example.demo.module.image.entity.ReviewImage;
import com.example.demo.module.user.dto.result.UserResult;
import com.example.demo.module.user.dto.result.UserSimpleResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

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

    @Schema(description = "해쉬태그 리스트")
    private List<HashtagResult> hashtagResult;

    @Schema(description = "생성자")
    private UserSimpleResult createdBy;

    @Schema(description = "작성 일자")
    private LocalDateTime createdDate;

    @Schema(description = "수정 일자")
    private LocalDateTime modifiedDate;

    @Schema(description = "리뷰 이미지")
    private List<ImageResult> imageResult;

    private Boolean isOwner;
}
