package com.example.demo.domain.entity;


import com.example.demo.domain.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserStorage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Users users;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Medicine medicine;

    private String medicineName;

    private LocalDateTime expirationDate;

    private String memo;
}
