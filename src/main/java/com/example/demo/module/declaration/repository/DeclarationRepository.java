package com.example.demo.module.declaration.repository;

import com.example.demo.module.declaration.entity.Declaration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeclarationRepository extends JpaRepository<Declaration, Long> {
    @EntityGraph(attributePaths = {"review","user","createdBy"})
    Page<Declaration> findAllByUserUserId(Long userId, PageRequest pageRequest);

    boolean existsByIdAndUserUserId(Long declarationId, Long userId);

    boolean existsByReviewIdAndUserUserId(Long reviewId, Long userId);

    @EntityGraph(attributePaths = {"review","user","createdBy"})
    Optional<Declaration> findByIdAndUserUserId(Long declarationId, Long userId);
}
