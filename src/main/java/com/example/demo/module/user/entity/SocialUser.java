package com.example.demo.module.user.entity;


import com.example.demo.module.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class SocialUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String socialId;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialEmail;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
