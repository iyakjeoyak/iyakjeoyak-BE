package com.example.demo.module.image.entity;


import com.example.demo.module.common.entity.BaseTimeEntity;
import com.example.demo.module.medicine.entity.Medicine;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MedicineImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    private Image image;
}
