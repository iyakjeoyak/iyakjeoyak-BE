package com.example.demo.module.pharmacy.dto.payload;

import com.example.demo.module.map.dto.result.BusinessHours;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PharmacyPayload {
    private String dutyName;

    private String dutyAddr;

    private String latitude;

    private String longitude;

    private String dutyTel1;

    private String hpid;

    private List<BusinessHours> businessHoursList = new ArrayList<>();
}
