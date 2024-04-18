package com.example.demo.module.user_storage.repository;

import com.example.demo.module.user_storage.entity.UserStorage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStorageRepository extends JpaRepository<UserStorage, Long> {
    Page<UserStorage> findAllByUserUserId(Long userId, Pageable pageable);
}