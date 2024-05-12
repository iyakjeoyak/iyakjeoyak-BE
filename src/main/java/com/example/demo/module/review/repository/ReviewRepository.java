package com.example.demo.module.review.repository;

import com.example.demo.module.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByMedicineId(Long medicineId, PageRequest pageRequest);

    Boolean existsByMedicineIdAndCreatedByUserId(Long medicineId, Long userId);

    Page<Review> findAllByCreatedByUserId(Long userId, PageRequest pageRequest);

    List<Review> findAllByCreatedDateBetween(LocalDateTime lastWeek, LocalDateTime now);
}
