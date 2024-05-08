package com.example.demo.module.point.dto.result;

import com.example.demo.module.common.result.PageResult;
import lombok.Data;

@Data
public class MyPointResult {
    private Integer point;
    private PageResult<PointHistoryResult> pageResult;
}
