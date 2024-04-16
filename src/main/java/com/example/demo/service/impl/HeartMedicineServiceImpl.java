package com.example.demo.service.impl;

import com.example.demo.domain.entity.HeartMedicine;
import com.example.demo.domain.entity.Medicine;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.HeartMedicineRepository;
import com.example.demo.domain.repository.MedicineRepository;
import com.example.demo.service.HeartMedicineService;
import com.example.demo.web.result.HeartMedicineResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class HeartMedicineServiceImpl implements HeartMedicineService {
    private final HeartMedicineRepository heartMedicineRepository;
    private final MedicineRepository medicineRepository;
//    private final UserRepository userRepository;
//    @Override
//    public Long like(Long medicineId, Long userId) {
//        Medicine medicine = medicineRepository.findById(medicineId)
//                .orElseThrow(() -> new NoSuchElementException("해당하는 영양제가 없습니다."));
//
//        long id = heartMedicineRepository.save(HeartMedicine.builder()
//                .medicine(medicine)
////                .user(userRepository.findById(userId).orElseThrow("해당 유저 없음."))
//                .build()).getId();
//
//        medicine.setHeartCount(medicine.getHeartCount()+1);
//        medicineRepository.save(medicine);
//        return id;
//    }

    @Override
    public Long like(Long medicineId, Long userId) {
        //Todo : UserRepository pull 받은 후 user 세팅하도록 변경 할 것

        if(!isChecked(medicineId, userId)) {
            Medicine medicine = medicineRepository.findById(medicineId)
                    .orElseThrow(() -> new NoSuchElementException("해당하는 영양제가 없습니다."));

            Long id = heartMedicineRepository.save(HeartMedicine.builder()
                    .medicine(medicine)
//                    .user(userRepository.findById(userId).orElseThrow())
                    .build()).getId();
            medicine.setHeartCount(medicine.getHeartCount() + 1);
            medicineRepository.save(medicine);
            return id;
        }
        throw new IllegalArgumentException("좋아요를 이미 누름");
    }

//    @Override
//    public Long cancel(Long medicineId, Long userId) {
//        Medicine medicine = medicineRepository.findById(medicineId)
//                .orElseThrow(() -> new NoSuchElementException("해당하는 영양제가 없습니다."));
//        HeartMedicine heartMedicine = heartMedicineRepository.findByMedicineId(medicineId)
//                .orElseThrow(() -> new NoSuchElementException("해당 게시물 좋아요 누른 유저가 아닙니다."));
//
//        medicine.setHeartCount(medicine.getHeartCount()-1);
//        medicineRepository.save(medicine);
//
//        heartMedicineRepository.deleteByMedicineIdAndUserUserId(medicineId, userId);
//        return medicineId;
//    }
    @Override
    @Transactional
    public Long cancel(Long medicineId, Long userId) {
        if(isChecked(medicineId, userId)){
            Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 영양제가 없습니다."));

           medicine.setHeartCount(medicine.getHeartCount()-1);
           medicineRepository.save(medicine);

           heartMedicineRepository.deleteByMedicineIdAndUserUserId(medicineId, userId);
           return medicineId;
        }
        throw new IllegalArgumentException("좋아요 누른 회원이 아님.");
    }

    @Override
    public List<HeartMedicineResult> findAll(Long userId) {
//        if(userRepository.existsById(userId)){
            return heartMedicineRepository.findAllByUserUserId(userId).stream().map(HeartMedicine::toDto).toList();
//        }
//          userNOtFound
    }

    @Override
    public Boolean isChecked(Long medicineId, Long userId) {
        return heartMedicineRepository.existsByMedicineIdAndUserUserId(medicineId, userId);
    }
}
