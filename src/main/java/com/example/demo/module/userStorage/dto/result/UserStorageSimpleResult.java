package com.example.demo.module.userStorage.dto.result;

import com.example.demo.module.image.dto.result.ImageResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStorageSimpleResult {

    @Schema(description = "보관 영양제 PK")
    private Long id;

    @Schema(description = "유저 설정 이름")
    private String medicineName;

    @Schema(description = "영양제 평점")
    private Double grade;

    @Schema(description = "유통기한")
    private String expirationDate;

    @Schema(description = "이미지")
    private ImageResult image;

}
