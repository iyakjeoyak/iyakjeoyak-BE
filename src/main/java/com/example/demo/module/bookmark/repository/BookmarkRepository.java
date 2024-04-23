package com.example.demo.module.bookmark.repository;

import com.example.demo.module.bookmark.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    @EntityGraph(attributePaths = {"medicine"})
    Page<Bookmark> findAllByUserUserId(Long userId, PageRequest pageRequest);

    boolean existsByMedicineIdAndUserUserId(Long medicineId, Long userId);

    Optional<Bookmark> findByMedicineIdAndUserUserId(Long medicineId, Long userId);

    boolean existsByIdAndUserUserId(Long bookmarkId, Long userId);
}
