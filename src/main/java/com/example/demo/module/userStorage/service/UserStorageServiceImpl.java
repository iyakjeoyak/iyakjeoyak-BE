package com.example.demo.module.userStorage.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.image.entity.Image;
import com.example.demo.module.image.service.ImageService;
import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.medicine.repository.MedicineRepository;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.user.repository.UserRepository;
import com.example.demo.module.userStorage.dto.payload.UserStorageCreatePayload;
import com.example.demo.module.userStorage.dto.payload.UserStorageEditPayload;
import com.example.demo.module.userStorage.dto.result.UserStorageDetailResult;
import com.example.demo.module.userStorage.dto.result.UserStorageSimpleResult;
import com.example.demo.module.userStorage.entity.UserStorage;
import com.example.demo.module.userStorage.repository.UserStorageRepository;
import com.example.demo.util.mapper.UserStorageDetailResultMapper;
import com.example.demo.util.mapper.UserStorageSimpleResultMapper;
import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;

import static com.example.demo.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserStorageServiceImpl implements UserStorageService {
    private final UserStorageRepository userStorageRepository;
    private final UserRepository userRepository;
    private final MedicineRepository medicineRepository;
    private final UserStorageSimpleResultMapper simpleResultMapper;
    private final UserStorageDetailResultMapper detailResultMapper;
    private final ImageService imageService;

    @Counted("my.user.storage")
    @Transactional
    @Override
    public Long saveUserStorage(Long userId, UserStorageCreatePayload userStorageCreatePayload, MultipartFile image) throws IOException {

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Medicine medicine = null;
        if (userStorageCreatePayload.getMedicineId() != null && userStorageCreatePayload.getMedicineId() != 0L) {
            medicine = medicineRepository.findById(userStorageCreatePayload.getMedicineId()).orElseThrow(() -> new CustomException(MEDICINE_NOT_FOUND));
        }

        Image saveImage = imageService.saveImage(image);

        if (saveImage == null && medicine != null) {
            saveImage = medicine.getImage();
        }

        UserStorage save = userStorageRepository.save(
                UserStorage.builder()
                        .user(user)
                        .medicine(medicine)
                        .medicineName(userStorageCreatePayload.getMedicineName())
                        .expirationDate(userStorageCreatePayload.getExpirationDate())
                        .image(saveImage)
                        .memo(userStorageCreatePayload.getMemo())
                        .build());

        return save.getId();
    }

    @Override
    public PageResult<UserStorageSimpleResult> getAllByUserId(Long userId, Pageable pageable) {
        Page<UserStorageSimpleResult> results = userStorageRepository.findAllByUserUserId(userId, pageable).map(simpleResultMapper::toDto);
        return new PageResult<>(results);
    }

    @Override
    public UserStorageDetailResult getOneById(Long userStorageId) {
        return detailResultMapper.toDto(
                userStorageRepository.findById(userStorageId).orElseThrow(() -> new CustomException(STORAGE_NOT_FOUND)));
    }

    @Transactional
    @Override
    public Long deleteById(Long userId, Long storageId) {
        if (!userStorageRepository.findById(storageId).orElseThrow(() -> new CustomException(STORAGE_NOT_FOUND))
                .getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("접근 유저가 잘못되었습니다.");
        }
        userStorageRepository.deleteById(storageId);
        return storageId;
    }

    @Transactional
    @Override
    public Long editUserStorage(Long userId, Long storageId, UserStorageEditPayload payload ,MultipartFile image) throws IOException {


        UserStorage userStorage = userStorageRepository.findById(storageId).orElseThrow(() -> new CustomException(STORAGE_NOT_FOUND));
        if (!userStorage.getUser().getUserId().equals(userId)) {
            throw new CustomException(ACCESS_BLOCKED);
        }

        Medicine medicine = null;
        if (payload.getMedicineId() != null) {
            medicine = medicineRepository.findById(payload.getMedicineId()).orElseThrow(() -> new CustomException(MEDICINE_NOT_FOUND));
        }

        // 수정 Image 를 받으면 이미지도 수정
        if (image != null && !image.isEmpty()) {
            //기존 이미지 삭제
            imageService.deleteImage(userId, userStorage.getImage().getId());

            //새로운 이미지 저장
            Image save = imageService.saveImage(image);
            userStorage.changeImage(save);
        }

        // 이미지 이외 정보 수정
        return userStorage.edit(medicine, payload.getMedicineName(), payload.getExpirationDate(), payload.getMemo());
    }

    @Override
    @Transactional
    public Long deleteStorageImage(Long userId, Long storageId, Long imageId) throws IOException {
        UserStorage userStorage = userStorageRepository.findById(storageId).orElseThrow(() -> new CustomException(STORAGE_NOT_FOUND));
        if (!userStorage.getUser().getUserId().equals(userId)) {
            throw new CustomException(ACCESS_BLOCKED);
        }
        if (!userStorage.getImage().getId().equals(imageId)) {
            throw new IllegalArgumentException("해당 보관함의 이미지가 아닙니다.");
        }
        imageService.deleteImage(userId, imageId);

        userStorage.deleteImage();
        return storageId;
    }


}
