package com.example.demo.service.impl;

import com.example.demo.domain.entity.Medicine;
import com.example.demo.domain.repository.MedicineRepository;
import com.example.demo.domain.repository.ReviewRepository;
import com.example.demo.service.MedicineService;
import com.example.demo.util.mapper.MedicineMapper;
import com.example.demo.web.payload.MedicinePayload;
import com.example.demo.web.result.MedicineResult;
import com.example.demo.web.result.PageResult;
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
    private final ReviewRepository reviewRepository;
    @Override
    public Long save(MedicinePayload medicinePayload) {
        return medicineRepository.save(Medicine.builder()
                .BSSH_NM(medicinePayload.getBssh_NM())
                .PRDLST_NM(medicinePayload.getPrdlst_NM())
                .build()).getId();
    }

    @Override
    public PageResult<MedicineResult> findAll(Pageable pageable) {
        Page<MedicineResult> result = medicineRepository.findAll(pageable)
                .map(Medicine::toDto);
        // todo 영양제 리뷰 계산해서 medicineResult에 넣기
        return new PageResult<>(result);
    }

    @Override
    public MedicineResult findOneById(Long medicineId) {
        return medicineMapper.toDto(medicineRepository.findById(medicineId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 영양제가 없습니다.")));
    }
}
