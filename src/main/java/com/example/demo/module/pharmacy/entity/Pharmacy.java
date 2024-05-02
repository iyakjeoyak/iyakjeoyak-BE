package com.example.demo.module.pharmacy.entity;

import com.example.demo.module.common.entity.BaseTimeEntity;
import com.example.demo.module.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Pharmacy extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String dutyName;

    private String dutyAddr;

    private String latitude;

    private String longitude;

    private String dutyTel1;

    private String hpid;
}