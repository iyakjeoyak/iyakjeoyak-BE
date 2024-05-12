package com.example.demo.module.medicine.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.medicine.dto.payload.MedicinePayload;
import com.example.demo.module.medicine.dto.result.MedicineResult;
import com.example.demo.module.medicine.dto.result.MedicineSimpleResult;
import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.medicine.repository.MedicineRepository;
import com.example.demo.util.mapper.MedicineMapper;
import com.example.demo.util.mapper.MedicineSimpleResultMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static com.example.demo.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicineServiceImplTest {

    @Mock
    private MedicineRepository medicineRepository;
    @Mock
    private MedicineMapper medicineMapper;
    @Mock
    private MedicineSimpleResultMapper medicineSimpleResultMapper;
    @InjectMocks
    private MedicineServiceImpl medicineService;

    @Test
    void save(){
        MedicinePayload medicinePayload = MedicinePayload.builder()
                .BSSH_NM("company").PRDLST_NM("product").build();
        Medicine medicine = Medicine.builder()
                .id(1L).BSSH_NM(medicinePayload.getBSSH_NM()).PRDLST_NM(medicinePayload.getPRDLST_NM()).build();

        when(medicineRepository.save(any(Medicine.class))).thenReturn(medicine);
        Long id = medicineService.save(medicinePayload);

        assertEquals(medicine.getId(), id);
    }
    @Test
    void findAll(){
        List<Medicine> medicines = Arrays.asList(
                Medicine.builder().id(1L).BSSH_NM("company1").PRDLST_NM("product1").build(),
                Medicine.builder().id(2L).BSSH_NM("company2").PRDLST_NM("product2").build(),
                Medicine.builder().id(3L).BSSH_NM("company3").PRDLST_NM("product3").build()
        );
        Page<Medicine> medicinePage = new PageImpl<>(medicines);
        Pageable pageable = PageRequest.of(0,3);
        when(medicineRepository.findAll(pageable)).thenReturn(medicinePage);
        when(medicineSimpleResultMapper.toDto(any(Medicine.class))).thenAnswer(invocation -> {
            Medicine medicine = invocation.getArgument(0);
            return MedicineSimpleResult.builder().BSSH_NM(medicine.getBSSH_NM()).PRDLST_NM(medicine.getPRDLST_NM()).build();
        });

        PageResult<MedicineSimpleResult> result = medicineService.findAll(pageable);

        assertEquals(3, result.getTotalElement());
        assertEquals(1, result.getTotalPages());

        List<MedicineSimpleResult> results = result.getData();
        assertEquals("company1", results.get(0).getBSSH_NM());
        assertEquals("product1", results.get(0).getPRDLST_NM());
        assertEquals("company2", results.get(1).getBSSH_NM());
        assertEquals("product2", results.get(1).getPRDLST_NM());
        assertEquals("company3", results.get(2).getBSSH_NM());
        assertEquals("product3", results.get(2).getPRDLST_NM());
    }
    @Test
    void findAllByQuery(){
    }
    @Test
    void findOneById(){
        Medicine medicine = Medicine.builder().build();
        MedicineResult medicineResult = MedicineResult.builder().id(1L).build();
        when(medicineRepository.findById(1L)).thenReturn(Optional.ofNullable(medicine));
        when(medicineMapper.toDto(any(Medicine.class))).thenReturn(medicineResult);

        MedicineResult actualMedicine = medicineService.findOneById(1L);

        assertEquals(medicineResult, actualMedicine);
    }
    @Test
    void findOneById_medicineNotFound(){
        Medicine medicine = Medicine.builder().id(1L).build();
        when(medicineRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> medicineService.findOneById(1L)).isInstanceOf(CustomException.class).hasMessage(MEDICINE_NOT_FOUND.getMessage());
    }
}