package com.example.demo.domain.repository;

import com.example.demo.domain.entity.MedicineImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineImageRepository extends JpaRepository<MedicineImage, Long> {
}
