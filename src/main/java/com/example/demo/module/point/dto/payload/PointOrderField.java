package com.example.demo.module.point.dto.payload;

import lombok.Getter;

@Getter
public enum PointOrderField {
    CREATED_DATE("createdDate") , ID("id") ,;
    private final String value;

    PointOrderField(String value) {
        this.value = value;
    }
}
