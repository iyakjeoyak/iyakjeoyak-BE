package com.example.demo.service;

import com.example.demo.domain.entity.User;
import com.example.demo.web.result.HeartMedicineResult;
import com.example.demo.web.result.PageResult;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HeartMedicineService {
//    Long like(Long medicineId, Long userId);
    Long like(Long medicineId, Long userId);
//    Long cancel(Long medicineId, Long userId);
    Long cancel(Long medicineId, Long userId);

    PageResult<HeartMedicineResult> findAll(Long userId, Pageable pageable);

    Boolean isChecked(Long medicineId, Long userId);
}
