package com.example.demo.module.heart_medicine.dto.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class HeartMedicineResult {

    @Schema(description = "영양제 좋아요 PK")
    private Long id;

    @Schema(description = "영양제 PK")
    private Long medicineId;
}
