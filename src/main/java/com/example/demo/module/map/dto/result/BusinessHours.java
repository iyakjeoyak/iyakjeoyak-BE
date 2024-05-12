package com.example.demo.module.map.dto.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessHours {
    private String dayOfWeek;
    private String startHour;
    private String endHour;
}
