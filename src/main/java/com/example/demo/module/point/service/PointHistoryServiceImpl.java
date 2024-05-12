package com.example.demo.module.point.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.point.dto.result.MyPointResult;
import com.example.demo.module.point.dto.result.PointHistoryResult;
import com.example.demo.module.point.entity.PointDomain;
import com.example.demo.module.point.entity.PointHistory;
import com.example.demo.module.point.entity.ReserveUse;
import com.example.demo.module.point.repository.PointHistoryRepository;
import com.example.demo.module.user.repository.UserRepository;
import com.example.demo.util.mapper.PointResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointHistoryServiceImpl implements PointHistoryService {
    private final PointHistoryRepository pointHistoryRepository;
    private final PointResultMapper pointResultMapper;
    private final UserRepository userRepository;


    @Override
    public MyPointResult findAllByUserId(Long userId, Pageable pageable) {
        MyPointResult myPointResult = new MyPointResult();
        myPointResult.setPoint(userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND)).getPoint());
        myPointResult.setPageResult(new PageResult<>(pointHistoryRepository.findAllByCreatedByUserId(userId, pageable).map(pointResultMapper::toDto)));
        return myPointResult;
    }

    @Override
    public PointHistoryResult savePointHistory(PointDomain domain, ReserveUse reserveUse, Integer changedValue, Integer pointSum, Long domainPk) {
        PointHistory save = pointHistoryRepository.save(
                PointHistory.builder().
                        reserveUse(reserveUse)
                        .pointSum(pointSum)
                        .changedValue(changedValue)
                        .domain(domain)
                        .domainPk(domainPk)
                        .build());
        return pointResultMapper.toDto(save);
    }

    @Override
    public PointHistoryResult saveDeletePointHistory(PointDomain domain, ReserveUse reserveUse, Integer changedValue, Integer pointSum, Long domainPk) {
        PointHistory save = pointHistoryRepository.save(PointHistory.builder()
                .domain(domain)
                .changedValue(changedValue)
                .pointSum(pointSum)
                .reserveUse(reserveUse)
                .domainPk(domainPk)
                .build());
        return pointResultMapper.toDto(save);
    }
}
