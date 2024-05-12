package com.example.demo.util.mapper;

import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.medicine.dto.result.MedicineResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicineMapper extends EntityMapper<MedicineResult, Medicine>{
}
