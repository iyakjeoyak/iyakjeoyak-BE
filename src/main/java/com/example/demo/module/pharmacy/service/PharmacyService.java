package com.example.demo.module.pharmacy.service;

import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.pharmacy.dto.payload.PharmacyPayload;
import com.example.demo.module.pharmacy.dto.result.PharmacyResult;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface PharmacyService {
    Long save(Long userId, PharmacyPayload pharmacyPayload);

    Long delete(Long userId, Long pharmacyId);

    PageResult<PharmacyResult> getAllByUserId(Long userId, PageRequest pageRequest);

    PharmacyResult getOneById(Long pharmacyId, Long userId);
}
