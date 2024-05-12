package com.example.demo.module.medicine_of_week.entity;

import com.example.demo.module.medicine.entity.Medicine;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class MedicineOfWeek {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Medicine medicine;

    private Integer year;

    private Integer week;

    private Integer ranking;
}
