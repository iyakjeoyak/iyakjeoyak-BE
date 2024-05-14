package com.example.demo.module.point.service;

import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.point.dto.result.MyPointResult;
import com.example.demo.module.point.dto.result.PointHistoryResult;
import com.example.demo.module.point.entity.PointDomain;
import com.example.demo.module.point.entity.ReserveUse;
import org.springframework.data.domain.Pageable;

public interface PointHistoryService {
    MyPointResult findAllByUserId(Long userId, Pageable pageable);

    PointHistoryResult savePointHistory(PointDomain domain, ReserveUse reserveUse, Integer changedValue, Integer pointSum, Long domainPk);

    PointHistoryResult saveDeletePointHistory(PointDomain domain, ReserveUse reserveUse, Integer changedValue, Integer pointSum, Long domainPk);

//    void cleanupExpiredPoint();
}
