package com.example.demo.module.userStorage.dto.payload;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserStorageCreatePayload {

    private Long medicineId;

    private String medicineName;

    private LocalDateTime expirationDate;

    private String memo;

    private MultipartFile image;

}
