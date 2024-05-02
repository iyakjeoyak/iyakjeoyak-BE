package com.example.demo.module.auto_complete.service;

import java.util.List;

public interface AutoCompleteService {
    List<String> getMedicineKeyword(String keyword, int size);
}
