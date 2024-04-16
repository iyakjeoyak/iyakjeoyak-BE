package com.example.demo.domain.repository;

import com.example.demo.domain.entity.HeartReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface HeartReviewRepository extends JpaRepository<HeartReview, Long> {
    Boolean existsByUserUserIdAndReviewId(Long userId, Long reviewId);

    Optional<HeartReview> findByUserUserIdAndReviewId(Long userId, Long reviewId);

    List<HeartReview> findAllByReviewId(Long reviewId);
}
