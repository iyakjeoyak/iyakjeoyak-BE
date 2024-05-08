package com.example.demo.module.userStorage.entity;


import com.example.demo.module.common.entity.BaseTimeEntity;
import com.example.demo.module.image.entity.Image;
import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserStorage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Image image;

    private String medicineName;

    private String expirationDate;

    private String memo;

    public Long edit(Medicine medicine, String medicineName, String expirationDate, String memo) {
        this.medicine = medicine;
        this.medicineName = medicineName;
        this.expirationDate = expirationDate;
        this.memo = memo;

        return this.getId();
    }

    public void changeImage(Image image) {
        this.image = image;
    }

    public Double getGrade() {
        return this.medicine.getGrade();
    }

    public void deleteImage() {
        this.image = null;
    }
}
