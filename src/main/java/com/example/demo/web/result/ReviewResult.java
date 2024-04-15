package com.example.demo.web.result;

import lombok.Data;

@Data
public class ReviewResult {
    private Long id;
    private String title;
    private String content;
    private Integer star;
    private Integer heartCount;
}
