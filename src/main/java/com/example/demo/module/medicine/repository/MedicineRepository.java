package com.example.demo.module.medicine.repository;

import com.example.demo.module.medicine.entity.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    @EntityGraph(attributePaths = {"image"})
    Page<Medicine> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"image"})
    Page<Medicine> findAllByIsAdTrue(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"image"})
    Optional<Medicine> findById(Long id);

}
