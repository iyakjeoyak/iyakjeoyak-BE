package com.example.demo.service;

import com.example.demo.domain.entity.User;
import com.example.demo.web.result.HeartMedicineResult;

import java.util.List;

public interface HeartMedicineService {
//    Long like(Long medicineId, Long userId);
    Long like(Long medicineId, User user);
//    Long cancel(Long medicineId, Long userId);
    Long cancel(Long medicineId, User user);

    List<HeartMedicineResult> findAll(Long userId);

    Boolean isChecked(Long medicineId, Long userId);
}
