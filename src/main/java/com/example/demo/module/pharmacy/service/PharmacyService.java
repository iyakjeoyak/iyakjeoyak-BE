package com.example.demo.module.pharmacy.service;

import com.example.demo.module.pharmacy.dto.payload.PharmacyPayload;
import com.example.demo.module.pharmacy.dto.result.PharmacyResult;

import java.util.List;

public interface PharmacyService {
    Long save(Long userId, PharmacyPayload pharmacyPayload);

    Long delete(Long userId, Long pharmacyId);

    List<PharmacyResult> getAllByUserId(Long userId);

    PharmacyResult getOneById(Long pharmacyId, Long userId);
}
