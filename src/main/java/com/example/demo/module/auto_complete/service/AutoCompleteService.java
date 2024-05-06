package com.example.demo.module.auto_complete.service;

import com.example.demo.module.auto_complete.dto.AutoCompleteResult;

import java.util.List;

public interface AutoCompleteService {
    List<AutoCompleteResult> getMedicineKeyword(String keyword, int size);
}
