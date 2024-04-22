package com.example.demo.module.medicine.dto.payload;

import lombok.Data;

@Data
public class MedicineSearchCond {
    private Integer heartCount;

    private Long categoryId;

    private Long hashtagId;

    private String keyword;

    // 정렬 어떻게 처리할지 고민중...
//    private OrderSortCond orderSortCond;

}
