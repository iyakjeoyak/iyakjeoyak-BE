package com.example.demo.module.medicine.service;

import com.example.demo.module.medicine.dto.payload.MedicinePayload;
import com.example.demo.module.medicine.dto.payload.MedicineSearchCond;
import com.example.demo.module.medicine.dto.result.MedicineResult;
import com.example.demo.module.medicine.dto.result.MedicineSimpleResult;
import com.example.demo.module.common.result.PageResult;
import org.springframework.data.domain.Pageable;

public interface MedicineService {
    Long save(MedicinePayload medicinePayload);

    PageResult<MedicineSimpleResult> findAll(Pageable pageable);

    PageResult<MedicineSimpleResult> findAllByQuery(MedicineSearchCond cond, Pageable pageable);

    MedicineResult findOneById(Long medicineId);

}
