package com.example.demo.module.declaration.entity;

import com.example.demo.module.declaration.dto.result.DeclarationResult;
import com.example.demo.module.review.entity.Review;
import com.example.demo.module.review.repository.ReviewRepository;
import com.example.demo.module.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Declaration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    public DeclarationResult toDto(){
        return DeclarationResult
                .builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .reviewId(this.review.getId())
                .build();
    }
}
