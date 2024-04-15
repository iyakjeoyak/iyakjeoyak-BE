package com.example.demo.domain.entity;

import com.example.demo.domain.entity.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private Integer star;

    private Integer heartCount;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    private Medicine medicine;

//    양방향 열건지 고려
//    @OneToMany
//    private List<ReviewHashtag> hashtagList = new ArrayList<>();

//    양방향 열건지 고려
//    @OneToMany
//    private List<Attachment> imageList = new ArrayList<>();

}
