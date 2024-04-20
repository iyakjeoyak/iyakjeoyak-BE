package com.example.demo.module.userStorage.service;

import com.example.demo.module.userStorage.dto.payload.UserStorageCreatePayload;
import com.example.demo.module.userStorage.dto.payload.UserStorageEditPayload;
import com.example.demo.module.userStorage.dto.result.UserStorageDetailResult;
import com.example.demo.module.userStorage.dto.result.UserStorageSimpleResult;
import com.example.demo.module.common.result.PageResult;

public interface UserStorageService {
    Long saveUserStorage(Long userId, UserStorageCreatePayload userStorageCreatePayload);

    PageResult<UserStorageSimpleResult> getAllByUserId(Long userId, Integer page, Integer size);

    UserStorageDetailResult getOneById(Long userStorageId);

    Long deleteById(Long userId ,Long storageId);

    Long editUserStorage(Long userId, Long storageId, UserStorageEditPayload payload);
}