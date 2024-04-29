package com.example.demo.module.point.dto.result;

import com.example.demo.module.point.entity.PointDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointHistoryResult {

    private Long id;

    private PointDomain domain;

    private Integer changedValue;

    private Integer pointSum;
}
