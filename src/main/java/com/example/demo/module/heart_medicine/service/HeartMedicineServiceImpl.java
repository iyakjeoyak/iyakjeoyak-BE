package com.example.demo.module.heart_medicine.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.heart_medicine.entity.HeartMedicine;
import com.example.demo.module.heart_medicine.repository.HeartMedicineRepository;
import com.example.demo.module.heart_medicine.dto.result.HeartMedicineResult;
import com.example.demo.module.medicine.repository.MedicineRepository;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.user.repository.UserRepository;
import com.example.demo.module.common.result.PageResult;
import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static com.example.demo.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HeartMedicineServiceImpl implements HeartMedicineService {
    private final HeartMedicineRepository heartMedicineRepository;
    private final MedicineRepository medicineRepository;
    private final UserRepository userRepository;

    @Counted("my.heart.medicine")
    @Override
    @Transactional
    public Long like(Long medicineId, Long userId) {
        if(!isChecked(medicineId, userId)) {
            Medicine medicine = medicineRepository.findById(medicineId)
                    .orElseThrow(() -> new CustomException(MEDICINE_NOT_FOUND));

            Long id = heartMedicineRepository.save(HeartMedicine.builder()
                    .medicine(medicine)
                    .user(userRepository.findById(userId).orElseThrow(()-> new CustomException(USER_NOT_FOUND)))
                    .build()).getId();
            medicine.setHeartCount(medicine.getHeartCount() + 1);
            medicineRepository.save(medicine);
            return id;
        }
        throw new CustomException(MEDICINE_HEART_EXIST);
    }

    @Override
    @Transactional
    public Long cancel(Long medicineId, Long userId) {
        if(isChecked(medicineId, userId)){
            Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new CustomException(MEDICINE_NOT_FOUND));
           medicine.setHeartCount(medicine.getHeartCount()-1);
           medicineRepository.save(medicine);

           heartMedicineRepository.deleteByMedicineIdAndUserUserId(medicineId, userId);
           return medicineId;
        }
        throw new CustomException(MEDICINE_HEART_EXIST);
    }

    @Override
    public PageResult<HeartMedicineResult> findAll(Long userId, Pageable pageable) {
        if(userRepository.existsById(userId)){
            Page<HeartMedicineResult> heartMedicines = heartMedicineRepository.findAllByUserUserId(userId, pageable).map(HeartMedicine::toDto);
            return new PageResult<>(heartMedicines);
        }
        throw new CustomException(USER_NOT_FOUND);
    }

    @Override
    public Boolean isChecked(Long medicineId, Long userId) {
        return heartMedicineRepository.existsByMedicineIdAndUserUserId(medicineId, userId);
    }

    @Counted("my.heart.medicine")
    @Override
    @Transactional
    public boolean click(Long medicineId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Medicine medicine = medicineRepository.findById(medicineId).orElseThrow(() -> new CustomException(MEDICINE_NOT_FOUND));
        if(isChecked(medicineId, userId)){
            medicine.decreaseHeartCount();
            Long heartMedicineId = heartMedicineRepository.findByMedicineIdAndUserUserId(medicineId, userId)
                    .orElseThrow(() -> new CustomException(MEDICINE_HEART_NOT_EXIST)).getId();
            heartMedicineRepository.deleteById(heartMedicineId);
            return false;
        }
        medicine.addHeartCount();
        heartMedicineRepository.save(HeartMedicine.builder()
                .medicine(medicine)
                .user(user)
                .build());
        return true;
    }
}
