package com.example.demo.module.review.dto.payload;

import lombok.Getter;

@Getter
public enum ReviewOrderField {
    HEART_COUNT("heartCount") , CREATED_DATE("createdDate") , ID("id") ,;
    private final String value;

    ReviewOrderField(String value) {
        this.value = value;
    }
}
