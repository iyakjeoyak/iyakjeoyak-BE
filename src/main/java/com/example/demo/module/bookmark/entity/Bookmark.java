package com.example.demo.module.bookmark.entity;

import com.example.demo.module.common.entity.BaseTimeEntity;
import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.medicine.repository.MedicineRepository;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.bookmark.dto.result.BookmarkResult;
import com.example.demo.util.mapper.MedicineMapper;
import com.example.demo.util.mapper.MedicineMapperImpl;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Bookmark extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public BookmarkResult toDto(MedicineMapper medicineMapper){
        return BookmarkResult
                .builder()
                .id(this.id)
                .medicine(medicineMapper.toDto(medicine))
                .build();
    }

}
