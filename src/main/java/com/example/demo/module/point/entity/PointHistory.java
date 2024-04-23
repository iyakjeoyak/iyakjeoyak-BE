package com.example.demo.module.point.entity;


import com.example.demo.module.common.entity.BaseEntity;
import com.example.demo.module.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PointHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String domain;

    @Enumerated(EnumType.STRING)
    private ReserveUse reserveUse;

    private Integer changedValue;

    private Integer pointSum;
}
