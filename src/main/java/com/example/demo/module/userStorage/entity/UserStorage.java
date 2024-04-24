package com.example.demo.module.userStorage.entity;


import com.example.demo.module.common.entity.BaseTimeEntity;
import com.example.demo.module.image.entity.Image;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.medicine.entity.Medicine;
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

    private LocalDateTime expirationDate;

    private String memo;

    public Long edit(Medicine medicine, String medicineName, LocalDateTime expirationDate, String memo, Image image) {
        this.medicine = medicine;
        this.medicineName = medicineName;
        this.expirationDate = expirationDate;
        this.memo = memo;
        this.image = image == null ? this.image : image;

        return this.getId();
    }
}
