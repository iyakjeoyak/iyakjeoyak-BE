package com.example.demo.service.impl;

import com.example.demo.domain.entity.Medicine;
import com.example.demo.domain.repository.MedicineRepository;
import com.example.demo.service.MedicineService;
import com.example.demo.web.payload.MedicinePayload;
import com.example.demo.web.result.MedicineResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicineServiceImpl implements MedicineService {
    private final MedicineRepository medicineRepository;

    @Override
    public Long save(MedicinePayload medicinePayload) {
        return medicineRepository.save(Medicine.builder()
                .BSSH_NM(medicinePayload.getBssh_NM())
                .PRDLST_NM(medicinePayload.getPrdlst_NM())
                .build()).getId();
    }

    @Override
    public Page<MedicineResult> findAll(Pageable pageable) {
        return medicineRepository.findAll(pageable).map(Medicine::toDto);
    }

    @Override
    public MedicineResult findOneById(Long medicineId) {
        return medicineRepository.findById(medicineId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 영양제가 없습니다.")).toDto();
    }
}
