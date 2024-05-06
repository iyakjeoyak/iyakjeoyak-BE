package com.example.demo.module.pharmacy.dto.result;

import com.example.demo.module.map.dto.result.BusinessHours;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PharmacyResult {
    private Long id;

    private String dutyName;

    private String dutyAddr;

    private String latitude;

    private String longitude;

    private String dutyTel1;

    private String hpid;

    private List<BusinessHours> businessHoursList = new ArrayList<>();
}
