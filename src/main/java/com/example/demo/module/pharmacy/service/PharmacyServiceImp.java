package com.example.demo.module.pharmacy.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.pharmacy.dto.payload.PharmacyPayload;
import com.example.demo.module.pharmacy.dto.result.PharmacyResult;
import com.example.demo.module.pharmacy.entity.Pharmacy;
import com.example.demo.module.pharmacy.repository.PharmacyRepository;
import com.example.demo.module.user.repository.UserRepository;
import com.example.demo.util.mapper.PharmacyResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.global.exception.ErrorCode.*;

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
                .telephone(pharmacyPayload.getTelephone())
                .hpid(pharmacyPayload.getHpid())
                .build()).getId();
    }

    @Override
    @Transactional
    public Long delete(Long userId, Long pharmacyId) {
        if(pharmacyRepository.existsByUserUserIdAndId(userId, pharmacyId)){
            pharmacyRepository.deleteById(pharmacyId);
            return pharmacyId;
        }
        throw new CustomException(ACCESS_BLOCKED);
    }

    @Override
    public PageResult<PharmacyResult> getAllByUserId(Long userId, PageRequest pageRequest) {
        Page<PharmacyResult> pharmacyResults = pharmacyRepository.findAllByUserUserId(userId, pageRequest).map(pharmacyResultMapper::toDto);
        return new PageResult<>(pharmacyResults);
    }

    @Override
    public PharmacyResult getOneById(Long pharmacyId, Long userId) {
        if(pharmacyRepository.existsByUserUserIdAndId(userId, pharmacyId)){
            return pharmacyResultMapper.toDto(pharmacyRepository
                    .findById(pharmacyId).orElseThrow(() -> new CustomException(PHARMACY_NOT_FOUND)));
        }
        throw new CustomException(ACCESS_BLOCKED);
    }
}
