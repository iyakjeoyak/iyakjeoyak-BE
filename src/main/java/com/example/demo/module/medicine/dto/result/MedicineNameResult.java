package com.example.demo.module.medicine.dto.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MedicineNameResult {
    @Schema(description = "영양제 PK")
    private Long id;

    @Schema(description = "브랜드 명")
    private String BSSH_NM;
}
