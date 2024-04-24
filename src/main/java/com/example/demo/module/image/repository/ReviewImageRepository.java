package com.example.demo.module.image.repository;

import com.example.demo.module.image.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    boolean existsByReviewIdAndImageId(Long reviewId, Long imageId);

    Optional<ReviewImage> findByReviewIdAndImageId(Long reviewId, Long imageId);
}
