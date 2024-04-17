package com.example.demo.web.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class UserStorageSimpleResult {
    private Long id;

    private String medicineName;

    private LocalDateTime expirationDate;

}
