package com.example.demo.service;

import com.example.demo.web.result.HeartMedicineResult;

import java.util.List;

public interface HeartMedicineService {
    Long like(Long medicineId, Long userId);

    Long cancel(Long medicineId, Long userId);

    List<HeartMedicineResult> findAll(Long userId);

    Boolean isChecked(Long medicineId, Long userId);
}
