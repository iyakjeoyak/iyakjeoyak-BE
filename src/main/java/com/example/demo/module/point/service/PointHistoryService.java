package com.example.demo.module.point.service;

import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.point.dto.result.PointHistoryResult;
import org.springframework.data.domain.Pageable;

public interface PointHistoryService {
    PageResult<PointHistoryResult> findAllByUserId(Long userId, Pageable pageable);

    void cleanupExpiredPoint();
}
