package com.example.demo.module.medicine_of_week.repository;

import com.example.demo.module.medicine_of_week.entity.MedicineOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineOfWeekRepository extends JpaRepository<MedicineOfWeek, Long> {

}
