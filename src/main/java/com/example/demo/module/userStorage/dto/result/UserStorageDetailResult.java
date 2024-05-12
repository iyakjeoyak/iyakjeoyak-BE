package com.example.demo.module.userStorage.dto.result;

import com.example.demo.module.image.dto.result.ImageResult;
import com.example.demo.module.medicine.dto.result.MedicineResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class UserStorageDetailResult {

    @Schema(description = "보관 영양제 PK")
    private Long id;

    @Schema(description = "보관 영양제 상세")
    private MedicineResult medicine;

    @Schema(description = "유저 세팅 이름")
    private String medicineName;

    @Schema(description = "유통기간")
    private String expirationDate;

    @Schema(description = "간략 메모")
    private String memo;

    @Schema(description = "이미지")
    private ImageResult image;
}
