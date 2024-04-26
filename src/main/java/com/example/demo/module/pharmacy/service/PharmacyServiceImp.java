package com.example.demo.module.pharmacy.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.module.pharmacy.dto.payload.PharmacyPayload;
import com.example.demo.module.pharmacy.dto.result.PharmacyResult;
import com.example.demo.module.pharmacy.entity.Pharmacy;
import com.example.demo.module.pharmacy.repository.PharmacyRepository;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.user.repository.UserRepository;
import com.example.demo.util.mapper.PharmacyResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static com.example.demo.global.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PharmacyServiceImp implements PharmacyService{
    private final PharmacyRepository pharmacyRepository;
    private final UserRepository userRepository;
    private final PharmacyResultMapper pharmacyResultMapper;
    @Override
    @Transactional
    public Long save(Long userId, PharmacyPayload pharmacyPayload) {
        return pharmacyRepository.save(Pharmacy.builder()
                .user(userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND)))
                .name(pharmacyPayload.getName())
                .latitude(pharmacyPayload.getLatitude())
                .longitude(pharmacyPayload.getLongitude())
                .build()).getId();
    }

    @Override
    @Transactional
    public Long delete(Long userId, Long pharmacyId) {
        if(pharmacyRepository.existsByUserUserIdAndId(userId, pharmacyId)){
            pharmacyRepository.deleteById(pharmacyId);
            return pharmacyId;
        }
        throw new IllegalArgumentException("해당유저가 저장한 약국이 아닙니다.");
    }

    @Override
    public List<PharmacyResult> getAllByUserId(Long userId) {
       List<Pharmacy> pharmacies = pharmacyRepository.findAllByUserUserId(userId);
       return pharmacyResultMapper.toDtoList(pharmacies);
    }

    @Override
    public PharmacyResult getOneById(Long pharmacyId, Long userId) {
        if(pharmacyRepository.existsByUserUserIdAndId(userId, pharmacyId)){
            return pharmacyResultMapper.toDto(pharmacyRepository
                    .findById(pharmacyId).orElseThrow(() -> new NoSuchElementException("약국 저장내역이 없습니다.")));
        }
        throw new IllegalArgumentException("해당 유저가 저장한 pharmacyId가 아닙니다.");
    }
}
