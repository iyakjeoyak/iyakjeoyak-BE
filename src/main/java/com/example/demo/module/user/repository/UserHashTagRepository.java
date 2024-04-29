package com.example.demo.module.user.repository;

import com.example.demo.module.user.entity.UserHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserHashTagRepository extends JpaRepository<UserHashtag, Long> {
}
