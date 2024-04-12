package com.example.demo.service;

import com.example.demo.web.result.MedicineResult;

import java.util.List;

public interface MedicineService {
    Long save(Long id);

    List<MedicineResult> findAll();

    MedicineResult findOneById(Long medicineId);

}
