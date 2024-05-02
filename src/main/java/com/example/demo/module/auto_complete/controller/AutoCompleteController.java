package com.example.demo.module.auto_complete.controller;

import com.example.demo.module.auto_complete.service.AutoCompleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auto-complete")
public class AutoCompleteController {
    private final AutoCompleteService autoCompleteService;

    @GetMapping
    public ResponseEntity<List<String>> autoComplete(
            @RequestParam("keyword") String keyword,
            @RequestParam(name = "size", defaultValue = "5", required = false) int size) {
        List<String> result = autoCompleteService.getMedicineKeyword(keyword, size);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
