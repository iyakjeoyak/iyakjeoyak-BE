package com.example.demo.module.review.repository;

import com.example.demo.module.review.entity.ReviewHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewHashtagRepository extends JpaRepository<ReviewHashtag, Long> {
    Optional<ReviewHashtag> findByReviewIdAndHashtagId(Long reviewId, Long hashtagId);

    Boolean existsByReviewIdAndHashtagId(Long reviewId, Long hashtagId);

    List<ReviewHashtag> findAllByReviewId(Long reviewId);
}
