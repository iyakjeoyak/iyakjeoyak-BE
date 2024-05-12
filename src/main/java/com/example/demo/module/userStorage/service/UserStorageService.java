package com.example.demo.module.userStorage.service;

import com.example.demo.module.userStorage.dto.payload.UserStorageCreatePayload;
import com.example.demo.module.userStorage.dto.payload.UserStorageEditPayload;
import com.example.demo.module.userStorage.dto.result.UserStorageDetailResult;
import com.example.demo.module.userStorage.dto.result.UserStorageSimpleResult;
import com.example.demo.module.common.result.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserStorageService {
    Long saveUserStorage(Long userId, UserStorageCreatePayload userStorageCreatePayload, MultipartFile image) throws IOException;

    PageResult<UserStorageSimpleResult> getAllByUserId(Long userId, Pageable pageable);

    UserStorageDetailResult getOneById(Long userStorageId);

    Long deleteById(Long userId ,Long storageId);

    Long editUserStorage(Long userId, Long storageId, UserStorageEditPayload payload, MultipartFile image) throws IOException;

    Long deleteStorageImage(Long userId, Long storageId, Long imageId) throws IOException;
}
