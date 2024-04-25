package com.example.demo.module.user.entity;


import com.example.demo.module.common.entity.BaseTimeEntity;
import com.example.demo.module.hashtag.entity.Hashtag;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseTimeEntity {

    //TODO 사용자 정보 벨리데이션 , 이미지 경로 , 소셜 값 필드 만들기
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String gender;

    private Integer age;

    //TODO enum or table 테이블을 추천드립니다. enum 으로하면 리스트 타입 관리가 어렵다.
    private String role;

    private Integer point;

    //TODO tag 부활? List<>, 연관관계
//    private String tag;

    @PrePersist
    public void init() {
        this.point = 0;
    }

    public Integer plusPoint(Integer point) {
        this.point += point;
        return this.point;
    }

    public Integer minusPoint(Integer point) {
        this.point -= point;
        return this.point;
    }
}
