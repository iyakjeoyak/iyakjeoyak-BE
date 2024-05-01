package com.example.demo.module.review.dto.result;

import com.example.demo.module.hashtag.dto.result.HashtagResult;
import com.example.demo.module.user.dto.result.UserSimpleResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ReviewDetailResult {
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

    @Schema(description = "생성자")
    private UserSimpleResult createdBy;

    @Schema(description = "작성 일자")
    private LocalDateTime createdDate;

    @Schema(description = "수정 일자")
    private LocalDateTime modifiedDate;

    @Schema(description = "영양제 PK")
    private Long medicineId;

    @Schema(description = "영양제 이름")
    private String medicineName;

    @Schema(description = "해쉬태그 리스트")
    private List<HashtagResult> hashtagResults = new ArrayList<>();
}
