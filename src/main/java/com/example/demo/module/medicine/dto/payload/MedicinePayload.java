package com.example.demo.module.medicine.dto.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MedicinePayload {
    @Schema(description = "브랜드 명")
    private String bssh_NM;

    @Schema(description = "제품 명")
    private String prdlst_NM;
}
