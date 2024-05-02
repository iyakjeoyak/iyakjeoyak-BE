package com.example.demo.module.auto_complete.service;

import com.example.demo.module.medicine.repository.QueryMedicineRepository;
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


    @Override
    public List<String> getMedicineKeyword(String keyword, int size) {
        return queryMedicineRepository.getAutoComplete(keyword, size);
    }

}
