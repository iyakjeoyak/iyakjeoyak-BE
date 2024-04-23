package com.example.demo.module.user.entity;


import com.example.demo.module.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

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
    private String role;

    private Integer point;

    //TODO tag 부활? List<>, 연관관계
//    private String tag;

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
}
