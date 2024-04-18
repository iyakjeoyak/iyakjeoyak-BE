package com.example.demo.module.point.service;

import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.point.dto.result.PointHistoryResult;
import com.example.demo.module.point.repository.PointHistoryRepository;
import com.example.demo.util.mapper.PointResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointHistoryServiceImpl implements PointHistoryService {
    private final PointHistoryRepository pointHistoryRepository;
    private final PointResultMapper pointResultMapper;

    @Override
    public PageResult<PointHistoryResult> findAllByUserId(Long userId, Pageable pageable) {
        return new PageResult<>(pointHistoryRepository.findAllByUserUserId(userId, pageable).map(pointResultMapper::toDto));
    }

}
