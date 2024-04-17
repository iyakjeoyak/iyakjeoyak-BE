package com.example.demo.module.user_storage.entity;


import com.example.demo.module.common.entity.BaseTimeEntity;
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

    private String medicineName;

    private LocalDateTime expirationDate;

    private String memo;

    public Long edit(Medicine medicine, String medicineName, LocalDateTime expirationDate, String memo) {
        this.medicine = medicine;
        this.medicineName = medicineName;
        this.expirationDate = expirationDate;
        this.memo = memo;

        return this.getId();
    }
}
