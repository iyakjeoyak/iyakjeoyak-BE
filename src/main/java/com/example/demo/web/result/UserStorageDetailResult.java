package com.example.demo.web.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class UserStorageDetailResult {
    private Long id;

    private MedicineResult medicine;

    private String medicineName;

    private LocalDateTime expirationDate;

    private String memo;
}
