package com.example.demo.domain.repository;

import com.example.demo.domain.entity.HeartMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeartMedicineRepository extends JpaRepository<HeartMedicine , Long> {
    Optional<HeartMedicine> findByMedicineId(Long medicineId);

    void deleteByMedicineIdAndUserUserId(Long medicineId, Long userId);

    Optional<List<HeartMedicine>> findAllByUserUserId(Long userId);

    Boolean existsByMedicineIdAndUserUserId(Long medicineId, Long userId);
}
