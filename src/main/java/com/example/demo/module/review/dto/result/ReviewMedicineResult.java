package com.example.demo.module.review.dto.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReviewMedicineResult {

    @Schema(description = "영양제 PK")
    private Long id;

    @Schema(description = "브랜드 명")
    private String BSSH_NM;

    @Schema(description = "제품 명")
    private String PRDLST_NM;

    //필요하면 열꺼
//    @Schema(description = "좋아요 수")
//    private Integer heartCount;
//
//    @Schema(description = "평균 평점")
//    private Double grade;
}
