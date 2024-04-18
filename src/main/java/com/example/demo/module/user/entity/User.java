package com.example.demo.module.user.entity;


import com.example.demo.module.common.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private String username;

    private String password;

    private String nickName;

    private String gender;

    private Integer age;

    private String role;
//    private String tag;

}
