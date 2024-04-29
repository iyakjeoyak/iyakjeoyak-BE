package com.example.demo.module.point.repository;

import com.example.demo.module.point.entity.PointHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    Page<PointHistory> findAllByCreatedByUserId(Long UserId, Pageable pageable);

    List<PointHistory> findByCreatedDateBefore(LocalDateTime localDateTime);

//    Optional<PointHistory> findByCreatedByUserIdAndReviewIdAndCreatedDateBefore(Long userId, Long reviewId, LocalDateTime localDateTime);
}
