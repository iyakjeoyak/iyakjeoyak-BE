package com.example.demo.module.medicine.dto.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MedicineSearchCond {

    @Schema(description = "카테고리 PK")
    private Long categoryId;

    @Schema(description = "해쉬태그 PK")
    private Long hashtagId;

    @Schema(description = "키워드 (회사명, 상품명)")
    private String keyword;

    @Schema(description = "정렬 기준")
    private OrderSortCond orderSortCond;

}
