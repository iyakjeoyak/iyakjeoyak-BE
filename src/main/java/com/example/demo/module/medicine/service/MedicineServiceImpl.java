package com.example.demo.module.medicine.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.medicine.dto.payload.MedicinePayload;
import com.example.demo.module.medicine.dto.payload.MedicineSearchCond;
import com.example.demo.module.medicine.dto.result.MedicineResult;
import com.example.demo.module.medicine.dto.result.MedicineSimpleResult;
import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.medicine.repository.MedicineRepository;
import com.example.demo.module.medicine.repository.QueryMedicineRepository;
import com.example.demo.util.mapper.MedicineMapper;
import com.example.demo.util.mapper.MedicineSimpleResultMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.example.demo.global.exception.ErrorCode.MEDICINE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicineServiceImpl implements MedicineService {
    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;
    private final MedicineSimpleResultMapper simpleResultMapper;
    private final QueryMedicineRepository queryMedicineRepository;

    @Override
    public Long save(MedicinePayload medicinePayload) {
        return medicineRepository.save(Medicine.builder()
                .BSSH_NM(medicinePayload.getBSSH_NM())
                .PRDLST_NM(medicinePayload.getPRDLST_NM())
                .build()).getId();
    }

    @Override
    public PageResult<MedicineSimpleResult> findAll(Pageable pageable) {
        Page<MedicineSimpleResult> result = medicineRepository.findAll(pageable).map(simpleResultMapper::toDto);
        return new PageResult<>(result);
    }

    @Override
    public PageResult<MedicineSimpleResult> findAllByQuery(MedicineSearchCond cond, Pageable pageable) {
        Page<MedicineSimpleResult> result = queryMedicineRepository.findAllBySearch(cond, pageable).map(simpleResultMapper::toDto);
        return new PageResult<>(result);
    }

    @Override
    public MedicineResult findOneById(Long medicineId) {
        return medicineMapper.toDto(medicineRepository.findById(medicineId)
                .orElseThrow(() -> new CustomException(MEDICINE_NOT_FOUND)));
    }

    @Override
    public PageResult<MedicineSimpleResult> findAllByIsAd(Pageable pageable) {
        Page<MedicineSimpleResult> map = medicineRepository.findAllByIsAdTrue(pageable).map(simpleResultMapper::toDto);
        return new PageResult<>(map);
    }

}
