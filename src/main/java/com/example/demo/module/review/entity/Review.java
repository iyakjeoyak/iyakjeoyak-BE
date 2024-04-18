package com.example.demo.module.review.entity;

import com.example.demo.module.common.entity.BaseTimeEntity;
import com.example.demo.module.image.entity.ReviewImage;
import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.review.dto.payload.ReviewEditPayload;
import com.example.demo.module.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    private Double star;

    private Integer heartCount;

//    EntityListener 사용할까 고민중
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Medicine medicine;


    // 해쉬태그와 이미지는 댓글이 삭제되면 함께 삭제
    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReviewHashtag> hashtagList = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReviewImage> imageList = new ArrayList<>();

    public Long update(ReviewEditPayload reviewEditPayload) {
        this.content = reviewEditPayload.getContent();
        this.title = reviewEditPayload.getTitle();
        this.star = reviewEditPayload.getStar();
        return this.id;
    }

    public void addHeartCount() {
        this.heartCount++;
    }

    public void decreaseHeartCount() {
        this.heartCount--;
        if(this.heartCount < 0) throw new IllegalArgumentException("좋아요가 0 이하가 되었습니다.(비정상)");
    }
}
