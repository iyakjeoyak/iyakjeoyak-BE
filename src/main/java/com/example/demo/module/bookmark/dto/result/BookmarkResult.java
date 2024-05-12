package com.example.demo.module.bookmark.dto.result;

import com.example.demo.module.medicine.dto.result.MedicineResult;
import com.example.demo.module.medicine.entity.Medicine;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BookmarkResult {
    @Schema(description = "북마크 PK")
    private Long id;

    @Schema(description = "영양제")
    private MedicineResult medicine;
}
