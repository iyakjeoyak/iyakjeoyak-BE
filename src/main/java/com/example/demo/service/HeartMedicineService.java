package com.example.demo.service;

import com.example.demo.domain.entity.HeartMedicine;
import com.example.demo.web.result.HeartMedicineResult;

import java.util.List;

public interface HeartMedicineService {
    Long like(long medicineId);

    Long cancel(Long medicineId);

    List<HeartMedicineResult> findAll();
}
