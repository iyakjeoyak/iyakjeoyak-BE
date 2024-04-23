package com.example.demo.module.medicine.dto.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OrderSortCond {

    @Schema(description = "정렬 필드명")
    private String orderField;

    @Schema(description = "정렬 기준 : 오름차순 (1) 내림차순(-1)")
    private Integer sort;
}
