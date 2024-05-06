package com.example.demo.module.pharmacy.repository;

import com.example.demo.module.pharmacy.entity.Pharmacy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

    @Query("SELECT DISTINCT p FROM Pharmacy p LEFT JOIN FETCH p.businessHoursList bhl WHERE p.user.userId = ?1")
    Page<Pharmacy> findAllByUserUserId(Long userId, PageRequest pageRequest);

    List<Pharmacy> findAllByUserUserId(Long userId);

    boolean existsByUserUserIdAndId(Long userId, Long pharmacyId);

    boolean existsByUserUserIdAndHpid(Long userId, String hpid);

    Optional<Pharmacy> findByUserUserIdAndHpid(Long userId, String hpid);
}
