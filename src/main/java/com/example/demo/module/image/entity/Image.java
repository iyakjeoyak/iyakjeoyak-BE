package com.example.demo.module.image.entity;

import com.example.demo.module.common.entity.BaseEntity;
import com.example.demo.module.common.entity.BaseTimeEntity;
import com.example.demo.module.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originName;

    private String storeName;
}
