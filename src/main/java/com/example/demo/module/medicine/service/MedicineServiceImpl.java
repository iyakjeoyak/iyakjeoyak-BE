package com.example.demo.module.medicine.service;

import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.medicine.repository.MedicineRepository;
import com.example.demo.module.medicine.dto.payload.MedicinePayload;
import com.example.demo.module.medicine.dto.result.MedicineResult;
import com.example.demo.module.medicine.dto.result.MedicineSimpleResult;
import com.example.demo.util.mapper.MedicineMapper;
import com.example.demo.util.mapper.MedicineSimpleResultMapper;
import com.example.demo.module.common.result.PageResult;
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
    private final MedicineMapper medicineMapper;
    private final MedicineSimpleResultMapper simpleResultMapper;
    @Override
    public Long save(MedicinePayload medicinePayload) {
        return medicineRepository.save(Medicine.builder()
                .BSSH_NM(medicinePayload.getBssh_NM())
                .PRDLST_NM(medicinePayload.getPrdlst_NM())
                .build()).getId();
    }

    @Override
    public PageResult<MedicineSimpleResult> findAll(Pageable pageable) {
        Page<MedicineSimpleResult> result = medicineRepository.findAll(pageable).map(simpleResultMapper::toDto);
        return new PageResult<>(result);
    }

    @Override
    public MedicineResult findOneById(Long medicineId) {
        return medicineMapper.toDto(medicineRepository.findById(medicineId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 영양제가 없습니다.")));
    }
}