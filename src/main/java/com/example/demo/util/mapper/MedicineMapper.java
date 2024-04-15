package com.example.demo.util.mapper;

import com.example.demo.domain.entity.Medicine;
import com.example.demo.web.result.MedicineResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicineMapper extends EntityMapper<MedicineResult, Medicine>{
}
