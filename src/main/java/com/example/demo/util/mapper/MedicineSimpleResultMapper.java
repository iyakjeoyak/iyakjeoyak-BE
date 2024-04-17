package com.example.demo.util.mapper;

import com.example.demo.domain.entity.Medicine;
import com.example.demo.web.result.MedicineSimpleResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicineSimpleResultMapper extends EntityMapper<MedicineSimpleResult, Medicine> {

}
