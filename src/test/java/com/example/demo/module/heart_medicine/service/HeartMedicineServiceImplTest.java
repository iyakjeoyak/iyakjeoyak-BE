package com.example.demo.module.heart_medicine.service;

import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.heart_medicine.dto.result.HeartMedicineResult;
import com.example.demo.module.heart_medicine.entity.HeartMedicine;
import com.example.demo.module.heart_medicine.repository.HeartMedicineRepository;
import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.medicine.repository.MedicineRepository;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HeartMedicineServiceImplTest {
    @Mock
    private MedicineRepository medicineRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private HeartMedicineRepository heartMedicineRepository;
    @InjectMocks
    private HeartMedicineServiceImpl heartMedicineService;
    private Long medicineId;
    private Long userId;

    @BeforeEach
    void setup(){
        medicineId = 1L;
        userId = 1L;
    }
    @Test
    void like(){
        Medicine medicine = Medicine.builder().id(medicineId).heartCount(0).build();
        User user = User.builder().userId(userId).build();
        HeartMedicine heartMedicine = HeartMedicine.builder().medicine(medicine).user(user).id(1L).build();
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.of(medicine));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(heartMedicineRepository.existsByMedicineIdAndUserUserId(medicineId, userId)).thenReturn(false);
        when(heartMedicineRepository.save(any(HeartMedicine.class))).thenReturn(heartMedicine);

        Long id = heartMedicineService.like(medicineId, userId);

        assertEquals(1L, id);
        assertEquals(1, medicine.getHeartCount());
    }
    @Test
    void cancel(){
        Medicine medicine = Medicine.builder().id(medicineId).heartCount(100).build();
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.of(medicine));
        when(heartMedicineRepository.existsByMedicineIdAndUserUserId(medicineId, userId)).thenReturn(true);

        Long id = heartMedicineService.cancel(medicineId, userId);

        assertEquals(1L, id);
        assertEquals(99, medicine.getHeartCount());
    }
    @Test
    void findAll(){
        User user = User.builder().userId(userId).build();
        List<Medicine> medicines = Arrays.asList(
                Medicine.builder().id(1L).BSSH_NM("company1").PRDLST_NM("product1").build(),
                Medicine.builder().id(3L).BSSH_NM("company2").PRDLST_NM("product2").build(),
                Medicine.builder().id(8L).BSSH_NM("company3").PRDLST_NM("product3").build()
        );
        List<HeartMedicine> heartMedicines = Arrays.asList(
                HeartMedicine.builder().id(1L).medicine(medicines.get(0)).user(user).build(),
                HeartMedicine.builder().id(2L).medicine(medicines.get(1)).user(user).build(),
                HeartMedicine.builder().id(3L).medicine(medicines.get(2)).user(user).build()
        );
        Page<HeartMedicine> medicinePage = new PageImpl<>(heartMedicines);
        PageResult<HeartMedicineResult> expected = new PageResult<>(medicinePage.map(HeartMedicine::toDto));
        List<HeartMedicineResult> expectedData = expected.getData();
        PageRequest pageRequest = PageRequest.of(0,3);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(heartMedicineRepository.findAllByUserUserId(userId, pageRequest)).thenReturn(medicinePage);

        PageResult<HeartMedicineResult> result = heartMedicineService.findAll(userId, pageRequest);
        List<HeartMedicineResult> resultData = result.getData();

        assertEquals(1, result.getTotalPages());
        assertEquals(3, result.getTotalElement());
        for(int i=0;i<3;i++){
            assertEquals(expectedData.get(i), resultData.get(i));
        }
    }
    @Test
    void isChecked(){
        when(heartMedicineRepository.existsByMedicineIdAndUserUserId(medicineId, userId)).thenReturn(true);
        when(heartMedicineRepository.existsByMedicineIdAndUserUserId(medicineId, 2L)).thenReturn(false);

        Boolean checked1 = heartMedicineService.isChecked(medicineId, userId);
        Boolean checked2 = heartMedicineService.isChecked(medicineId, 2L);

        assertTrue(checked1);
        assertFalse(checked2);

    }
}