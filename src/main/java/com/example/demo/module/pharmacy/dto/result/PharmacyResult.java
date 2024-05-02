package com.example.demo.module.pharmacy.dto.result;

import lombok.Data;

@Data
public class PharmacyResult {
    private Long id;

    private String dutyName;

    private String dutyAddr;

    private String latitude;

    private String longitude;

    private String dutyTel1;

    private String hpid;
}
