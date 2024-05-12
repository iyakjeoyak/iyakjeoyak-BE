package com.example.demo.module.point.entity;

import lombok.Getter;
import org.springframework.data.relational.core.mapping.Embedded;

@Getter
public enum ReserveUse {
    RESERVE , USE , CANCELED
}
