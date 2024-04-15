package com.example.demo.service.impl;

import com.example.demo.domain.entity.HeartMedicine;
import com.example.demo.domain.entity.Medicine;
import com.example.demo.domain.repository.HeartMedicineRepository;
import com.example.demo.domain.repository.MedicineRepository;
import com.example.demo.service.HeartMedicineService;
import com.example.demo.web.result.HeartMedicineResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class HeartMedicineServiceImpl implements HeartMedicineService {
    private final HeartMedicineRepository heartMedicineRepository;
    private final MedicineRepository medicineRepository;
    @Override
    public Long like(long medicineId) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 영양제가 없습니다."));

        long id = heartMedicineRepository.save(HeartMedicine.builder()
                .medicine(medicine)
                .build()).getId();

        medicine.setHeartCount(medicine.getHeartCount()+1);
        medicineRepository.save(medicine);
        return id;
    }

    @Override
    public Long cancel(Long medicineId) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 영양제가 없습니다."));
        HeartMedicine heartMedicine = heartMedicineRepository.findByMedicineId(medicineId)
                .orElseThrow(() -> new NoSuchElementException("해당 게시물 좋아요 누른 유저가 아닙니다."));

        medicine.setHeartCount(medicine.getHeartCount()-1);
        medicineRepository.save(medicine);

        heartMedicineRepository.deleteById(heartMedicine.getId());
        return medicineId;
    }

    @Override
    public List<HeartMedicineResult> findAll() {
        return heartMedicineRepository.findAll().stream().map(HeartMedicine::toDto).toList();
    }


}
