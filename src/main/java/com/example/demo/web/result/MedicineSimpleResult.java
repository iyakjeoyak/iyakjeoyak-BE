package com.example.demo.web.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MedicineSimpleResult {

    @Schema(description = "영양제 PK")
    private Long id;

    @Schema(description = "브랜드 명")
    private String BSSH_NM;

    @Schema(description = "제품 명")
    private String PRDLST_NM;

    @Schema(description = "카테고리 정보")
    private List<CategoryResult> categories = new ArrayList<>();

    @Schema(description = "해쉬태그 정보")
    private List<HashtagResult> hashtags = new ArrayList<>();
}
