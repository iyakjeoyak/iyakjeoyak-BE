package com.example.demo.util.mapper;

import com.example.demo.module.userStorage.entity.UserStorage;
import com.example.demo.module.userStorage.dto.result.UserStorageSimpleResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserStorageSimpleResultMapper extends EntityMapper<UserStorageSimpleResult , UserStorage> {
}
