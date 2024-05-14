package com.example.demo.module.top_user.repository;

import com.example.demo.module.top_user.entity.TopUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopUserRepository extends JpaRepository<TopUser, Long> {

    List<TopUser> findByYearAndWeekOrderByRanking(int year, int week);

    Integer countAllByUserUserId(Long userId);
}
