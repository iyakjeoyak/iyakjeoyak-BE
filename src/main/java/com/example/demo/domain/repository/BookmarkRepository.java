package com.example.demo.domain.repository;

import com.example.demo.domain.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Page<Bookmark> findAllByUserUserId(Long userId, PageRequest pageRequest);

    boolean existsByMedicineIdAndUserUserId(Long medicineId, Long userId);

    Optional<Bookmark> findByMedicineIdAndUserUserId(Long medicineId, Long userId);

    boolean existsByIdAndUserUserId(Long bookmarkId, Long userId);
}
