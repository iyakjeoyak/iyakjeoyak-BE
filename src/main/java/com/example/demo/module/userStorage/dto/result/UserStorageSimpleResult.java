package com.example.demo.module.userStorage.dto.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class UserStorageSimpleResult {

    @Schema(description = "보관 영양제 PK")
    private Long id;

    @Schema(description = "유저 설정 이름")
    private String medicineName;

    @Schema(description = "유통기한")
    private LocalDateTime expirationDate;

}
