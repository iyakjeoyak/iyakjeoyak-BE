package com.example.demo.module.pharmacy.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.module.common.result.PageResult;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.demo.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
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
    private PharmacyServiceImp pharmacyService;

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
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.ofNullable(user));
        when(pharmacyRepository.save(any(Pharmacy.class))).thenReturn(pharmacy);

        Long id = pharmacyService.save(user.getUserId(), pharmacyPayload);

        assertEquals(pharmacy.getId(), id);
    }
    @Test
    void save_userNotFound(){
        PharmacyPayload pharmacyPayload = new PharmacyPayload();
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pharmacyService.save(user.getUserId(), pharmacyPayload)).isInstanceOf(CustomException.class).hasMessage(USER_NOT_FOUND.getMessage());
    }
    @Test
    void delete() {
        when(pharmacyRepository.existsByUserUserIdAndId(user.getUserId(), pharmacy.getId())).thenReturn(true);

        Long id = pharmacyService.delete(user.getUserId(), pharmacy.getId());

        assertEquals(pharmacy.getId(), id);
    }
    @Test
    void delete_userNotAccess(){
        when(pharmacyRepository.existsByUserUserIdAndId(user.getUserId(), pharmacy.getId())).thenReturn(false);

        assertThatThrownBy(() -> pharmacyService.delete(user.getUserId(), pharmacy.getId())).isInstanceOf(CustomException.class).hasMessage(ACCESS_BLOCKED.getMessage());
    }

    @Test
    void getAllByUserId() {
        Pharmacy pharmacy1 = Pharmacy.builder().id(1L).user(user).build();
        Pharmacy pharmacy2 = Pharmacy.builder().id(2L).user(user).build();
        Pharmacy pharmacy3 = Pharmacy.builder().id(3L).user(user).build();
        PharmacyResult pharmacyResult1 = new PharmacyResult();
        PharmacyResult pharmacyResult2 = new PharmacyResult();
        PharmacyResult pharmacyResult3 = new PharmacyResult();
        pharmacyResult1.setDutyName("약국1");
        pharmacyResult1.setDutyName("약국2");
        pharmacyResult1.setDutyName("약국3");


        PageRequest pageRequest = PageRequest.of(0, 3);
        List<Pharmacy> pharmacies = Arrays.asList(pharmacy1, pharmacy2, pharmacy3);
        Page<Pharmacy> pharmacyPage = new PageImpl<>(pharmacies);
        List<PharmacyResult> pharmacyResults = Arrays.asList(pharmacyResult1, pharmacyResult2, pharmacyResult3);
        PageResult<PharmacyResult> expected = new PageResult<>(new PageImpl<>(pharmacyResults));

        when(pharmacyRepository.findAllByUserUserId(user.getUserId(), pageRequest)).thenReturn(pharmacyPage);
        for(int i=0;i<3;i++){
            when(pharmacyResultMapper.toDto(pharmacies.get(i))).thenReturn(pharmacyResults.get(i));
        }
        PageResult<PharmacyResult> result = pharmacyService.getAllByUserId(user.getUserId(), pageRequest);

        assertEquals(expected, result);
    }

    @Test
    void getOneById() {
        when(pharmacyRepository.existsByUserUserIdAndId(user.getUserId(), pharmacy.getId())).thenReturn(true);
        when(pharmacyRepository.findById(pharmacy.getId())).thenReturn(Optional.ofNullable(pharmacy));
        PharmacyResult expected = pharmacyResultMapper.toDto(pharmacy);

        PharmacyResult pharmacyResult = pharmacyService.getOneById(pharmacy.getId(), user.getUserId());

        assertEquals(expected, pharmacyResult);
    }
    @Test
    void getOneById_userNotAccess(){
        when(pharmacyRepository.existsByUserUserIdAndId(user.getUserId(), pharmacy.getId())).thenReturn(false);

        assertThatThrownBy(() -> pharmacyService.getOneById(pharmacy.getId(), user.getUserId())).isInstanceOf(CustomException.class).hasMessage(ACCESS_BLOCKED.getMessage());
    }
    @Test
    void getOneById_pharmacyNotFound(){
        when(pharmacyRepository.existsByUserUserIdAndId(user.getUserId(), pharmacy.getId())).thenReturn(true);
        when(pharmacyRepository.findById(pharmacy.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pharmacyService.getOneById(pharmacy.getId(), user.getUserId())).isInstanceOf(CustomException.class).hasMessage(PHARMACY_NOT_FOUND.getMessage());
    }
}