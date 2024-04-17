package com.example.demo.service;

import com.example.demo.web.payload.UserStorageCreatePayload;
import com.example.demo.web.payload.UserStorageEditPayload;
import com.example.demo.web.result.PageResult;
import com.example.demo.web.result.UserStorageDetailResult;
import com.example.demo.web.result.UserStorageSimpleResult;

import java.awt.print.Pageable;

public interface UserStorageService {
    Long saveUserStorage(Long userId, UserStorageCreatePayload userStorageCreatePayload);

    PageResult<UserStorageSimpleResult> getAllByUserId(Long userId, Integer page, Integer size);

    UserStorageDetailResult getOneById(Long userStorageId);

    Long deleteById(Long storageId);

    Long editUserStorage(Long userId, Long storageId, UserStorageEditPayload payload);
}
