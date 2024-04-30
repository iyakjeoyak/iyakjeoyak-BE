package com.example.demo.module.medicine.dto.payload;

import com.querydsl.core.types.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OrderSortCond {

    @Schema(description = "정렬 필드명 enum 타입 : GRADE , HEART_COUNT , CREATED_DATE ")
    private MedicineOrderField medicineOrderField;

    @Schema(description = "정렬 기준 enum 타입 : 오름차순 ASC / 내림차순 DESC")
    private Order sort;
}
