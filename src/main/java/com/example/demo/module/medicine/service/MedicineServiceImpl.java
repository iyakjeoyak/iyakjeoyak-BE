package com.example.demo.module.medicine.service;

import com.example.demo.global.advice.aop.annotation.MyLog;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.hashtag.entity.Hashtag;
import com.example.demo.module.medicine.dto.payload.MedicinePayload;
import com.example.demo.module.medicine.dto.payload.MedicineSearchCond;
import com.example.demo.module.medicine.dto.result.MedicineOfWeekResult;
import com.example.demo.module.medicine.dto.result.MedicineResult;
import com.example.demo.module.medicine.dto.result.MedicineSimpleResult;
import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.medicine.repository.MedicineRepository;
import com.example.demo.module.medicine.repository.QueryMedicineRepository;
import com.example.demo.module.medicine_of_week.entity.MedicineOfWeek;
import com.example.demo.module.medicine_of_week.repository.MedicineOfWeekRepository;
import com.example.demo.module.user.repository.UserRepository;
import com.example.demo.util.mapper.MedicineMapper;
import com.example.demo.util.mapper.MedicineSimpleResultMapper;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.demo.global.exception.ErrorCode.*;
import static com.example.demo.global.exception.ErrorCode.MEDICINE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicineServiceImpl implements MedicineService {
    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;
    private final MedicineSimpleResultMapper simpleResultMapper;
    private final QueryMedicineRepository queryMedicineRepository;
    private final MedicineOfWeekRepository medicineOfWeekRepository;
    private final UserRepository userRepository;

    @Override
    public Long save(MedicinePayload medicinePayload) {
        return medicineRepository.save(Medicine.builder()
                .BSSH_NM(medicinePayload.getBSSH_NM())
                .PRDLST_NM(medicinePayload.getPRDLST_NM())
                .build()).getId();
    }
    @MyLog
    @Override
    public PageResult<MedicineSimpleResult> findAll(Pageable pageable) {
        Page<MedicineSimpleResult> result = medicineRepository.findAll(pageable).map(simpleResultMapper::toDto);
        return new PageResult<>(result);
    }

    @MyLog
    @Timed("my.medicine")
    @Override
    public PageResult<MedicineSimpleResult> findAllByQuery(MedicineSearchCond cond, Pageable pageable) {
        Page<MedicineSimpleResult> result = queryMedicineRepository.findAllBySearch(cond, pageable).map(simpleResultMapper::toDto);
        return new PageResult<>(result);
    }

    @MyLog
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

    @Override
    public List<MedicineOfWeekResult> getMedicineOfWeek() {
        int week = LocalDateTime.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
        List<MedicineOfWeek> result = medicineOfWeekRepository.findAllByWeek(week);
        return result.stream().map(mw -> new MedicineOfWeekResult(mw.getRanking(), simpleResultMapper.toDto(mw.getMedicine()))).toList();
    }

    @Override
    public List<MedicineResult> getRecommend(Long userId, Integer size) {
        List<Long> hashtagIds = new ArrayList<>();
        if (userId != null && userId != 0L) {
            hashtagIds = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND)).getHashtagList().stream().map(Hashtag::getId).toList();
        }
        List<Medicine> list = queryMedicineRepository.findRecommend(hashtagIds, size);

        return medicineMapper.toDtoList(list);
    }

}
