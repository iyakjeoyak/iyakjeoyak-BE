package com.example.demo.module.user.entity;


import com.example.demo.module.common.entity.BaseTimeEntity;
import com.example.demo.module.hashtag.entity.Hashtag;
import com.example.demo.module.user.dto.payload.UserEditPayload;
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
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Integer age;

    // 한 줄 소개
    private String introduce;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserRole> userRoleList = new ArrayList<>();

    private Integer point;

    // 중간 테이블 두기 태그는 여러개
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserHashtag> userHashTagList = new ArrayList<>();

    @PrePersist
    public void init() {
        this.point = 0;
    }

    public void editUser(UserEditPayload userEditPayload) {
        this.nickname = userEditPayload.getNickname();
        this.introduce = userEditPayload.getIntroduce();
        this.gender = userEditPayload.getGender();
        this.age = userEditPayload.getAge();
//        this.userHashTagList = userEditPayload.getUserHashtagList();
    }

    public Integer reviewPoint(Integer point) {
        this.point += point;
        return this.point;
    }

    public Integer minusPoint(Integer point) {
        this.point -= point;
        return this.point;
    }

    public List<Hashtag> getHashtagList() {
       return this.userHashTagList.stream().map(UserHashtag::getHashtag).toList();
    }
}
