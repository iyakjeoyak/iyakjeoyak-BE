package com.example.demo.module.pharmacy.service;

import com.example.demo.module.pharmacy.dto.payload.PharmacyPayload;
import com.example.demo.module.pharmacy.dto.result.PharmacyResult;
import com.example.demo.module.pharmacy.entity.Pharmacy;
import com.example.demo.module.pharmacy.repository.PharmacyRepository;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.user.repository.UserRepository;
import com.example.demo.util.mapper.PharmacyResultMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class PharmacyServiceImpTest {
    @Mock
    private PharmacyRepository pharmacyRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PharmacyResultMapper pharmacyResultMapper;
    @InjectMocks
    private PharmacyServiceImp pharmacyServiceImp;

    User user;
    Pharmacy pharmacy;
    @BeforeEach
    void setup(){
        user = User.builder().userId(1L).build();
        pharmacy = Pharmacy.builder().id(1L).user(user).build();
    }
    @Test
    void save() {
        PharmacyPayload pharmacyPayload = new PharmacyPayload();
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(pharmacyRepository.save(any(Pharmacy.class))).thenReturn(pharmacy);

        Long id = pharmacyServiceImp.save(user.getUserId(), pharmacyPayload);

        assertEquals(pharmacy.getId(), id);
    }

    @Test
    void delete() {
        when(pharmacyRepository.existsByUserUserIdAndId(user.getUserId(), pharmacy.getId())).thenReturn(true);

        Long id = pharmacyServiceImp.delete(user.getUserId(), pharmacy.getId());

        assertEquals(pharmacy.getId(), id);
    }

//    @Test
//    void getAllByUserId() {
//        Pharmacy pharmacy1 = Pharmacy.builder().id(1L).user(user).build();
//        Pharmacy pharmacy2 = Pharmacy.builder().id(2L).user(user).build();
//        Pharmacy pharmacy3 = Pharmacy.builder().id(3L).user(user).build();
//        PharmacyResult pharmacyResult1 = new PharmacyResult();
//        PharmacyResult pharmacyResult2 = new PharmacyResult();
//        PharmacyResult pharmacyResult3 = new PharmacyResult();
//        pharmacyResult1.setName("약국1");
//        pharmacyResult2.setName("약국2");
//        pharmacyResult3.setName("약국3");
//
//        List<Pharmacy> pharmacies = Arrays.asList(pharmacy1, pharmacy2, pharmacy3);
//        pharmacies.forEach(p -> when);
//        System.out.println("pharmacies = " + pharmacies);
//        List<PharmacyResult> expected = Arrays.asList(pharmacy1, pharmacy2, pharmacy3).stream().map().toList();
//        System.out.println("expected = " + expected);
//
//        when(pharmacyRepository.findAllByUserUserId(user.getUserId())).thenReturn(pharmacies);
//        List<PharmacyResult> result = pharmacyServiceImp.getAllByUserId(user.getUserId());
//
//        assertEquals(expected, result);
//    }

    @Test
    void getOneById() {
        when(pharmacyRepository.existsByUserUserIdAndId(user.getUserId(), pharmacy.getId())).thenReturn(true);
        when(pharmacyRepository.findById(pharmacy.getId())).thenReturn(Optional.ofNullable(pharmacy));
        PharmacyResult expected = pharmacyResultMapper.toDto(pharmacy);

        PharmacyResult pharmacyResult = pharmacyServiceImp.getOneById(pharmacy.getId(), user.getUserId());

        assertEquals(expected, pharmacyResult);
    }
}