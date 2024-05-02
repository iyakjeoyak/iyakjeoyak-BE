package com.example.demo.module.medicine.dto.result;

import com.example.demo.module.medicine_of_week.entity.MedicineOfWeek;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicineOfWeekResult {
    private Integer ranking;
    private MedicineSimpleResult medicine;
}
