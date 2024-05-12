package com.example.demo.module.review.entity;


import com.example.demo.module.common.entity.BaseTimeEntity;
import com.example.demo.module.hashtag.entity.Hashtag;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ReviewHashtag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Hashtag hashtag;

    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;
}
