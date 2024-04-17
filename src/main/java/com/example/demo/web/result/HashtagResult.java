package com.example.demo.web.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class HashtagResult {
    private Long id;
    private String name;
}
