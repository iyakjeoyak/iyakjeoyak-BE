package com.example.demo.module.point.dto.payload;

import com.example.demo.module.point.entity.ReserveUse;
import com.example.demo.module.point.entity.PointDomain;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointHistoryPayload {
    private PointDomain domain;

    private ReserveUse reserveUse;

    private Integer changedValue;

    private Integer pointSum;
}
