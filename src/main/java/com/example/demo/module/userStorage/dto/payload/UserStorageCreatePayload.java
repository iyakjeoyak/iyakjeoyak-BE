package com.example.demo.module.userStorage.dto.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserStorageCreatePayload {

    @Schema(description = "영양제 PK")
    private Long medicineId;

    @Schema(description = "보관함 타이틀")
    private String medicineName;

    @Schema(description = "유통기한")
    private String expirationDate;

    @Schema(description = "간략 메모")
    private String memo;
//
//    @Schema(description = "대표 이미지")
//    private MultipartFile image;

}
