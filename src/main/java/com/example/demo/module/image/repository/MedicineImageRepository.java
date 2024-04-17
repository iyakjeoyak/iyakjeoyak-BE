package com.example.demo.module.image.repository;

import com.example.demo.module.image.entity.MedicineImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineImageRepository extends JpaRepository<MedicineImage, Long> {
}
