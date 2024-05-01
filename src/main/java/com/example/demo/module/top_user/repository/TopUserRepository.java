package com.example.demo.module.top_user.repository;

import com.example.demo.module.top_user.entity.TopUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopUserRepository extends JpaRepository<TopUser, Long> {
    Optional<TopUser> findByUserUserId(Long userId);

    List<TopUser> findByYearAndWeek(int year, int week);
}
