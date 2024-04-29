package com.example.demo.module.pharmacy.repository;

import com.example.demo.module.pharmacy.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
    List<Pharmacy> findAllByUserUserId(Long userId);

    boolean existsByUserUserIdAndId(Long userId, Long pharmacyId);
}
