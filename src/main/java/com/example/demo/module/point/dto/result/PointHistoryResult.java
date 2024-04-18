package com.example.demo.module.point.dto.result;

import lombok.Data;

@Data
public class PointHistoryResult {

    private Long id;

    private String domain;

    private Integer changedValue;

    private Integer pointSum;
}
