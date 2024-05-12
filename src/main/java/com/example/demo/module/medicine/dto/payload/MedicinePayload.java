package com.example.demo.module.medicine.dto.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MedicinePayload {
    @Schema(description = "브랜드 명")
    private String BSSH_NM;

    @Schema(description = "제품 명")
    private String PRDLST_NM;
}
