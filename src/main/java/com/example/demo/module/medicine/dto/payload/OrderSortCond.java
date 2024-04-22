package com.example.demo.module.medicine.dto.payload;

import lombok.Data;

@Data
public class OrderSortCond {
    private String orderField;

    private Integer sort;
}
