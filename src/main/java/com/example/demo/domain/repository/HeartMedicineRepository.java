package com.example.demo.domain.repository;

import com.example.demo.domain.entity.HeartMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeartMedicineRepository extends JpaRepository<HeartMedicine , Long> {
}
