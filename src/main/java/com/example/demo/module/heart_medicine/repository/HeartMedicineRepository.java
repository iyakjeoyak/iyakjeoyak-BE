package com.example.demo.module.heart_medicine.repository;

import com.example.demo.module.heart_medicine.entity.HeartMedicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HeartMedicineRepository extends JpaRepository<HeartMedicine, Long> {
    void deleteByMedicineIdAndUserUserId(Long medicineId, Long userId);

    Page<HeartMedicine> findAllByUserUserId(Long userId, Pageable pageable);

    Boolean existsByMedicineIdAndUserUserId(Long medicineId, Long userId);

    Optional<HeartMedicine> findByMedicineIdAndUserUserId(Long medicineId, Long userId);

    @EntityGraph(attributePaths = {"medicine"})
    List<HeartMedicine> findAllByCreatedDateBetween(LocalDateTime lastWeek, LocalDateTime now);
}
