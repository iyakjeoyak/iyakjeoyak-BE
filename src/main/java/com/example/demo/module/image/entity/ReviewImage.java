package com.example.demo.module.image.entity;


import com.example.demo.module.common.entity.BaseTimeEntity;
import com.example.demo.module.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ReviewImage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    private String originName;

    private String storeName;
}