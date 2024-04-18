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

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String username;

    //TODO 비밀번호 정규식
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자리 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @NotBlank(message = "성별 선택은 필수 입력 값입니다.")
    private String gender;

    private Integer age;

    //TODO enum or table
    private String role;

    private Integer point;

    //TODO tag 부활?
//    private String tag;

    @PrePersist
    public void init() {
        this.point = 0;
    }

    public Integer reviewPoint(Integer point) {
        this.point += point;
        return point;
    }
}
