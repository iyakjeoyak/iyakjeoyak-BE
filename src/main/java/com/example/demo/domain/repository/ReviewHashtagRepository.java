package com.example.demo.domain.repository;

import com.example.demo.domain.entity.ReviewHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewHashtagRepository extends JpaRepository<ReviewHashtag,Long> {
}
