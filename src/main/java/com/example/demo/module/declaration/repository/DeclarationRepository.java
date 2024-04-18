package com.example.demo.module.declaration.repository;

import com.example.demo.module.declaration.entity.Declaration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeclarationRepository extends JpaRepository<Declaration, Long> {

    Page<Declaration> findAllByUserUserId(Long userId, PageRequest pageRequest);

    boolean existsByIdAndUserUserId(Long declarationId, Long userId);

    boolean existsByReviewIdAndUserUserId(Long reviewId, Long userId);
}
