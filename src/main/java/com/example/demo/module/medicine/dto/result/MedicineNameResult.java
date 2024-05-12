package com.example.demo.module.medicine.dto.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MedicineNameResult {
    @Schema(description = "영양제 PK")
    private Long id;

    @Schema(description = "제품 명")
    private String PRDLST_NM;
}
