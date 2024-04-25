package com.example.demo.module.point.service;

import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.point.dto.result.PointHistoryResult;
import com.example.demo.module.point.entity.PointHistory;
import com.example.demo.module.point.repository.PointHistoryRepository;
import com.example.demo.module.user.entity.User;
import com.example.demo.util.mapper.PointResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.example.demo.module.point.entity.ReserveUse.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointHistoryServiceImpl implements PointHistoryService {
    private final PointHistoryRepository pointHistoryRepository;
    private final PointResultMapper pointResultMapper;

    @Value("${point.review}")
    private Integer point;
    @Override
    public PageResult<PointHistoryResult> findAllByUserId(Long userId, Pageable pageable) {
        return new PageResult<>(pointHistoryRepository.findAllByCreatedByUserId(userId, pageable).map(pointResultMapper::toDto));
    }

    @Override
    @Transactional
    public void cleanupExpiredPoint() {
        LocalDateTime time = LocalDateTime.now().minusMinutes(1);
        for (PointHistory pointHistory : pointHistoryRepository.findByCreatedDateBefore(time)) {
            User user = pointHistory.getCreatedBy();
            if(pointHistory.getReserveUse().equals(RESERVE)){
                user.minusPoint(point);
                pointHistoryRepository.delete(pointHistory);
            }
        }
    }

}
