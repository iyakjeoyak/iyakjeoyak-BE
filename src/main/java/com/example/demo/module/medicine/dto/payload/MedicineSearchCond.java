package com.example.demo.module.medicine.dto.payload;

import lombok.Data;

@Data
public class MedicineSearchCond {
    private Integer heartCount;

    private Long categoryId;

    private Long hashtagId;

    private String keyword;

    private OrderSortCond orderSortCond;

}
