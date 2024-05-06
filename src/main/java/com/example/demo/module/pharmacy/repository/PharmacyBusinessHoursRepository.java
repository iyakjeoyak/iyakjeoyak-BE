package com.example.demo.module.pharmacy.repository;

import com.example.demo.module.pharmacy.entity.PharmacyBusinessHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PharmacyBusinessHoursRepository extends JpaRepository<PharmacyBusinessHours, Long> {
}
