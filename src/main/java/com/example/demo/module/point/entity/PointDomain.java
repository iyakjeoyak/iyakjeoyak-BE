package com.example.demo.module.point.entity;

import lombok.Getter;

@Getter
public enum PointDomain {
    REVIEW(10),
    HEART(3),
    ;
    // 각 도메인별 포인트
    private final Integer point;

    PointDomain(Integer point) {
        this.point = point;
    }
}
