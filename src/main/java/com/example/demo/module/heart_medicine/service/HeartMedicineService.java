package com.example.demo.module.heart_medicine.service;

import com.example.demo.module.heart_medicine.dto.result.HeartMedicineResult;
import com.example.demo.module.common.result.PageResult;
import org.springframework.data.domain.Pageable;

public interface HeartMedicineService {
    Long like(Long medicineId, Long userId);

    Long cancel(Long medicineId, Long userId);

    PageResult<HeartMedicineResult> findAll(Long userId, Pageable pageable);

    Boolean isChecked(Long medicineId, Long userId);

    boolean click(Long medicineId, Long userId);
}
