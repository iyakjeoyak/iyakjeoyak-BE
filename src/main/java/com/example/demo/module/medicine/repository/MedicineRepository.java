package com.example.demo.module.medicine.repository;

import com.example.demo.module.medicine.dto.result.MedicineSimpleResult;
import com.example.demo.module.medicine.entity.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    Page<Medicine> findAll(Pageable pageable);

    Page<Medicine> findAllByIsAdTrue(Pageable pageable);
}
