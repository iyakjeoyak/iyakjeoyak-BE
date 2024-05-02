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

    private String name;

    private String latitude;

    private String longitude;

    private String telephone;

    private String hpid;
}
// "dutyAddr": "경기도 평택시 평남로 951, 디에이치빌딩 1동 1층 103호 (비전동)",
//         "dutyName": "에브리데이항상약국",
//         "dutyTel1": "031-657-1325",
//         "hpid": "C2108009",
//         "wgs84Lat": 37.00527352664589,
//         "wgs84Lon": 127.10342277849394