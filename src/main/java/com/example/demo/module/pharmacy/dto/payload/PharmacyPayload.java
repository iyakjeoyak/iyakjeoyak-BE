package com.example.demo.module.pharmacy.dto.payload;

import lombok.Data;

@Data
public class PharmacyPayload {
    private String dutyName;

    private String dutyAddr;

    private String latitude;

    private String longitude;

    private String dutyTel1;

    private String hpid;
}
