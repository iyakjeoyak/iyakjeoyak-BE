package com.example.demo.module.heart_medicine.entity;


import com.example.demo.module.common.entity.BaseTimeEntity;
import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.heart_medicine.dto.result.HeartMedicineResult;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class HeartMedicine extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public HeartMedicineResult toDto(){
        return HeartMedicineResult.builder()
                .id(this.id)
                .medicineId(this.medicine.getId())
                .build();
    }
}
