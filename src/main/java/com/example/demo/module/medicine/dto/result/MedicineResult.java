package com.example.demo.module.medicine.dto.result;

import com.example.demo.module.category.dto.result.CategoryResult;
import com.example.demo.module.hashtag.dto.result.HashtagResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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

    @Schema(description = "기능성 원재료")
    private String INDIV_RAWMTRL_NM;

    @Schema(description = "좋아요 수")
    private Integer heartCount;

    @Schema(description = "평점")
    private Double grade;

    @Schema(description = "기능")
    private String PRIMARY_FNCLTY;

    @Schema(description = "복용 방법")
    private String NTK_MTHD;

    @Schema(description = "복용시 주의사항")
    private String IFTKN_ATNT_MATR_CN;

    @Schema(description = "카테고리 정보")
    private List<CategoryResult> categories = new ArrayList<>();

    @Schema(description = "해쉬태그 정보")
    private List<HashtagResult> hashtags = new ArrayList<>();
}
