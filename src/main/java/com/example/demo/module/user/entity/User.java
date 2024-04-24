package com.example.demo.module.user.entity;


import com.example.demo.module.common.entity.BaseTimeEntity;
import com.example.demo.module.hashtag.entity.Hashtag;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

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

    //TODO enum or table
/*    @OneToOne(mappedBy = "user")
    private Role role;*/

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

    public Integer reviewPoint(Integer point) {
        this.point += point;
        return this.point;
    }

    public Integer cancelReviewPoint(Integer point) {
        this.point -= point;
        return this.point;
    }

    public List<Hashtag> getHashtagList() {
       return this.userHashTagList.stream().map(UserHashtag::getHashtag).toList();
    }
}
