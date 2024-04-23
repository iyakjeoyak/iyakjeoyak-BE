package com.example.demo.module.medicine.dto.result;

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
    private Long grade;

    @Schema(description = "기능")
    private String PRIMARY_FNCLTY;

    @Schema(description = "복용 방법")
    private String NTK_MTHD;

    @Schema(description = "복용시 주의사항")
    private String IFTKN_ATNT_MATR_CN;

    @Schema(description = "원재료")
    private String INDIV_RAWMTRL_NM;

    @Schema(description = "리뷰 수")
    private Integer reviewCount;
}
