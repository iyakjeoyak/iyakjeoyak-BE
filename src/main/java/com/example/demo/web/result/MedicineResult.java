package com.example.demo.web.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MedicineResult {

    @Schema(description = "영양제 PK")
    private Long id;

    @Schema(description = "브랜드 명")
    private String BSSH_NM;

    @Schema(description = "제품 명")
    private String PRDLST_NM;

    @Schema(description = "좋아요 수")
    private Integer heartCount;

    @Schema(description = "평점")
    private Long star;

    @Schema(description = "리뷰 수")
    private Integer reviewCount;
}
