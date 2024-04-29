package com.example.demo.util.mapper;

import com.example.demo.module.pharmacy.dto.result.PharmacyResult;
import com.example.demo.module.pharmacy.entity.Pharmacy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PharmacyResultMapper extends EntityMapper<PharmacyResult, Pharmacy>{
}
