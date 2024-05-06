package com.example.demo.module.auto_complete.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.module.medicine.repository.QueryMedicineRepository;
import io.micrometer.common.util.StringUtils;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutoCompleteServiceImpl implements AutoCompleteService{
    private final QueryMedicineRepository queryMedicineRepository;

    @Timed("my.complete")
    @Override
    public List<String> getMedicineKeyword(String keyword, int size) {
        if (StringUtils.isEmpty(keyword) || 2 > keyword.length() || keyword.length() > 20) {
            throw new CustomException(ErrorCode.OUT_OF_LENGTH);
        }
        return queryMedicineRepository.getAutoComplete(keyword, size);
    }

}
