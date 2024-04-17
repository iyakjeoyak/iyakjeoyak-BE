package com.example.demo.domain.entity;

import com.example.demo.domain.entity.common.BaseTimeEntity;
import com.example.demo.web.result.BookmarkResult;
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

    public BookmarkResult toDto(){
        return BookmarkResult
                .builder()
                .id(this.id)
                .medicineId(this.medicine.getId())
                .build();
    }

}
