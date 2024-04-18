package com.example.demo.module.user_storage.service;

import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.medicine.repository.MedicineRepository;
import com.example.demo.module.user_storage.entity.UserStorage;
import com.example.demo.module.user_storage.repository.UserStorageRepository;
import com.example.demo.module.user_storage.dto.payload.UserStorageCreatePayload;
import com.example.demo.module.user_storage.dto.payload.UserStorageEditPayload;
import com.example.demo.module.user_storage.dto.result.UserStorageDetailResult;
import com.example.demo.module.user_storage.dto.result.UserStorageSimpleResult;
import com.example.demo.module.user.repository.UserRepository;
import com.example.demo.util.mapper.UserStorageDetailResultMapper;
import com.example.demo.util.mapper.UserStorageSimpleResultMapper;
import com.example.demo.module.common.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserStorageServiceImpl implements UserStorageService {
    private final UserStorageRepository userStorageRepository;
    private final UserRepository userRepository;
    private final MedicineRepository medicineRepository;
    private final UserStorageSimpleResultMapper simpleResultMapper;
    private final UserStorageDetailResultMapper detailResultMapper;

    @Transactional
    @Override
    public Long saveUserStorage(Long userId, UserStorageCreatePayload userStorageCreatePayload) {
        //Todo : 이미지 받아서 저장하는 로직 추가해야 함

        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("유저를 찾지 못했습니다."));
        Medicine medicine = null;
        if (userStorageCreatePayload.getMedicineId() != null) {
            medicine = medicineRepository.findById(userStorageCreatePayload.getMedicineId()).orElseThrow(() -> new NoSuchElementException("해당하는 영양제가 없습니다."));
        }

        UserStorage save = userStorageRepository.save(
                UserStorage.builder()
                        .user(user)
                        .medicine(medicine)
                        .medicineName(userStorageCreatePayload.getMedicineName())
                        .expirationDate(userStorageCreatePayload.getExpirationDate())
                        .memo(userStorageCreatePayload.getMemo())
                        .build());
        return save.getId();
    }

    @Override
    public PageResult<UserStorageSimpleResult> getAllByUserId(Long userId, Integer page, Integer size) {
        Page<UserStorageSimpleResult> results = userStorageRepository.findAllByUserUserId(userId, PageRequest.of(page, size)).map(simpleResultMapper::toDto);
        return new PageResult<>(results);
    }

    @Override
    public UserStorageDetailResult getOneById(Long userStorageId) {
        return detailResultMapper.toDto(
                userStorageRepository.findById(userStorageId).orElseThrow(() -> new NoSuchElementException("영양제 저장 내역이 없습니다.")));
    }

    @Override
    public Long deleteById(Long userId, Long storageId) {
        if (!userStorageRepository.findById(storageId).orElseThrow(() -> new NoSuchElementException("보관된 영양제를 찾을 수 없습니다."))
                .getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("접근 유저가 잘못되었습니다.");
        }
        userStorageRepository.deleteById(storageId);
        return storageId;
    }

    @Transactional
    @Override
    public Long editUserStorage(Long userId, Long storageId, UserStorageEditPayload payload) {
        //ToDo : 이미지 수정 처리도 넣을 예정

        UserStorage userStorage = userStorageRepository.findById(storageId).orElseThrow(() -> new NoSuchElementException("보관 내역이 없습니다."));
        if (!userStorage.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("작성자가 아니면 수정할 수 없습니다.");
        }

        Medicine medicine = null;
        if (payload.getMedicineId() != null) {
            medicine = medicineRepository.findById(payload.getMedicineId()).orElseThrow(() -> new NoSuchElementException("해당하는 영양제가 없습니다."));
        }

        return userStorage.edit(medicine, payload.getMedicineName(), payload.getExpirationDate(), payload.getMemo());
    }

}
