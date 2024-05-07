package com.example.demo.module.pharmacy.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.pharmacy.dto.payload.PharmacyPayload;
import com.example.demo.module.pharmacy.dto.result.PharmacyResult;
import com.example.demo.module.pharmacy.entity.Pharmacy;
import com.example.demo.module.pharmacy.entity.PharmacyBusinessHours;
import com.example.demo.module.pharmacy.repository.PharmacyBusinessHoursRepository;
import com.example.demo.module.pharmacy.repository.PharmacyRepository;
import com.example.demo.module.user.repository.UserRepository;
import com.example.demo.util.mapper.PharmacyResultMapper;
import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PharmacyServiceImp implements PharmacyService{
    private final PharmacyRepository pharmacyRepository;
    private final PharmacyBusinessHoursRepository pharmacyBusinessHoursRepository;
    private final UserRepository userRepository;
    private final PharmacyResultMapper pharmacyResultMapper;

    @Counted("my.pharmacy")
    @Override
    @Transactional
    public Long save(Long userId, PharmacyPayload pharmacyPayload) {
        if (userId == null) {
            throw new CustomException(USER_NOT_FOUND);
        }
        if (pharmacyRepository.existsByUserUserIdAndHpid(userId, pharmacyPayload.getHpid())) {
            Long pharmacyId = pharmacyRepository.findByUserUserIdAndHpid(userId, pharmacyPayload.getHpid()).orElseThrow().getId();
            pharmacyRepository.deleteById(pharmacyId);
            return pharmacyId;
        }
        Pharmacy save = pharmacyRepository.save(Pharmacy.builder()
                .user(userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND)))
                .dutyAddr(pharmacyPayload.getDutyAddr())
                .dutyName(pharmacyPayload.getDutyName())
                .dutyTel1(pharmacyPayload.getDutyTel1())
                .hpid(pharmacyPayload.getHpid())
                .longitude(pharmacyPayload.getLongitude())
                .latitude(pharmacyPayload.getLatitude())
                .build());

        pharmacyPayload.getBusinessHoursList().forEach(bh -> pharmacyBusinessHoursRepository.save(
                PharmacyBusinessHours.builder()
                        .dayOfWeek(bh.getDayOfWeek())
                        .startHour(bh.getStartHour())
                        .endHour(bh.getEndHour())
                        .pharmacy(save)
                        .build()));

        return save.getId();
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
