package com.example.demo.module.review.entity;

import com.example.demo.module.common.entity.BaseEntity;
import com.example.demo.module.image.entity.ReviewImage;
import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.review.dto.payload.ReviewEditPayload;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private Double star;

    private Integer heartCount;


    @ManyToOne(fetch = FetchType.LAZY)
    private Medicine medicine;


    // 해쉬태그와 이미지는 댓글이 삭제되면 함께 삭제
    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReviewHashtag> hashtagList = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReviewImage> imageList = new ArrayList<>();

    public void update(ReviewEditPayload reviewEditPayload) {
        this.content = reviewEditPayload.getContent();
        this.title = reviewEditPayload.getTitle();
        this.star = reviewEditPayload.getStar();
        medicine.gradeAvg();
    }

    public void addHeartCount() {
        this.heartCount++;
    }

    public void decreaseHeartCount() {
        this.heartCount--;
        if (this.heartCount < 0) throw new IllegalArgumentException("좋아요가 0 이하가 되었습니다.(비정상)");
    }

    @PostPersist
    public void postPersist() {
        medicine.gradeAvg();
    }


    @PreRemove
    public void preRemove() {
//        medicine.gradeAvg();
        medicine.gradeAvgByDelete(this.star);
    }
}
