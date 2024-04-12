package com.example.demo.service.impl;

import com.example.demo.domain.entity.Medicine;
import com.example.demo.domain.repository.MedicineRepository;
import com.example.demo.service.MedicineService;
import com.example.demo.web.result.MedicineResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {
    private final MedicineRepository medicineRepository;


    @Override
    public Long save(Long id) {
        return null;
    }

    @Override
    public List<MedicineResult> findAll() {
        return medicineRepository.findAll().stream().map(Medicine::toDto).toList();
    }
}
