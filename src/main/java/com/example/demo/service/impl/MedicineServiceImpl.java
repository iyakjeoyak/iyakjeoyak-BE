package com.example.demo.service.impl;

import com.example.demo.domain.entity.Medicine;
import com.example.demo.domain.repository.MedicineRepository;
import com.example.demo.service.MedicineService;
import com.example.demo.web.result.MedicineResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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

    @Override
    public MedicineResult findOneById(Long medicineId) {
        return medicineRepository.findById(medicineId).orElseThrow(() -> new NoSuchElementException("해당하는 영양제가 없습니다.")).toDto();
    }
}
