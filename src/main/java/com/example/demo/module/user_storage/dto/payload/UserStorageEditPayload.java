package com.example.demo.module.user_storage.dto.payload;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserStorageEditPayload {

    private Long medicineId;

    private String medicineName;

    private LocalDateTime expirationDate;

    private String memo;

    private List<MultipartFile> image;
}