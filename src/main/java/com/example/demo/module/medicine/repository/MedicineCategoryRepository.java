package com.example.demo.module.medicine.repository;

import com.example.demo.module.medicine.entity.MedicineCategory;
import com.example.demo.module.medicine.entity.MedicineHashtag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicineCategoryRepository extends JpaRepository<MedicineCategory, Long> {
    @Override
    @EntityGraph(attributePaths = {"category"})
    Optional<MedicineCategory> findById(Long id);
}
