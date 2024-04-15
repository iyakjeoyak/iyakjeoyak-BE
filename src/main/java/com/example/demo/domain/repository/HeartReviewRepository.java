package com.example.demo.domain.repository;

import com.example.demo.domain.entity.HeartReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeartReviewRepository extends JpaRepository<HeartReview, Long> {
}
