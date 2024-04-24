package com.example.demo.module.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO 유저가 여러가지 role을 부여 받을 수도 있다?
    private String name;

/*    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;*/
}
