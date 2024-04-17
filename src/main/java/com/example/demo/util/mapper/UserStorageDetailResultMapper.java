package com.example.demo.util.mapper;

import com.example.demo.module.user_storage.entity.UserStorage;
import com.example.demo.module.user_storage.dto.result.UserStorageDetailResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserStorageDetailResultMapper extends EntityMapper<UserStorageDetailResult, UserStorage>{
}
