package com.example.demo.service;

import com.example.demo.web.payload.MedicinePayload;
import com.example.demo.web.result.MedicineResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MedicineService {
    Long save(MedicinePayload medicinePayload);

    Page<MedicineResult> findAll(Pageable pageable);

    MedicineResult findOneById(Long medicineId);

}
