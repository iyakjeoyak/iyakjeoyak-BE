package com.example.demo.util.mapper;

import com.example.demo.module.point.dto.result.PointHistoryResult;
import com.example.demo.module.point.entity.PointHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PointResultMapper extends EntityMapper<PointHistoryResult, PointHistory>{
}
