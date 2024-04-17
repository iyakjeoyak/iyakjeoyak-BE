package com.example.demo.util.mapper;

import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.medicine.dto.result.MedicineSimpleResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicineSimpleResultMapper extends EntityMapper<MedicineSimpleResult, Medicine> {

}
