package com.example.demo.module.user.entity;


import com.example.demo.module.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;

    private String password;

    private String nickName;

    private String gender;

    private Integer age;

    private String role;

    private Integer point;
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
