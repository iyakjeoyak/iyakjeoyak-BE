package com.example.demo.module.userStorage.repository;

import com.example.demo.module.userStorage.entity.UserStorage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStorageRepository extends JpaRepository<UserStorage, Long> {
    @EntityGraph(attributePaths = {"user", "medicine", "image"})
    Page<UserStorage> findAllByUserUserId(Long userId, Pageable pageable);
}
