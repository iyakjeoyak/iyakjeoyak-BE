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

    private String domain;

    @Enumerated(EnumType.STRING)
    private ReserveUse reserveUse;

    private Integer changedValue;

    private Integer pointSum;

    //ToDo (준혁) : 아예 위의 도메인 컬럼을 뺴고 연관관계를 갖는거도 고려해볼만 할 듯
    private Long reviewId;
}
