package com.example.demo.module.userStorage.dto.payload;

import lombok.Getter;

@Getter
public enum StorageOrderField {
    MEDICINE_ID("medicineId"), ID("id"), MEDICINE_NAME("medicineName"), EXPIRATION_DATE("expirationDate"), CREATED_DATE("createdDate"),
    ;

    private String value;

    StorageOrderField(String value) {
        this.value = value;
    }
}
