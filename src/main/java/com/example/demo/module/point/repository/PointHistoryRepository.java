package com.example.demo.module.point.repository;

import com.example.demo.module.point.entity.PointHistory;
import com.example.demo.module.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    Page<PointHistory> findAllByCreatedByUserId(Long UserId, Pageable pageable);

//    Optional<PointHistory> findByCreatedByUserIdAndReviewIdAndCreatedDateBefore(Long userId, Long reviewId, LocalDateTime localDateTime);

    @Query("SELECT ph.createdBy FROM PointHistory ph WHERE ph.createdDate > :time GROUP BY ph.createdBy ORDER BY SUM(ph.changedValue) DESC, ph.pointSum DESC")
    List<User> findTop3UserAndSumChangedValue(@Param("time") LocalDateTime time);
}
