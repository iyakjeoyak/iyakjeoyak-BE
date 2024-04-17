package com.example.demo.util.mapper;

import com.example.demo.domain.entity.UserStorage;
import com.example.demo.web.result.UserStorageDetailResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserStorageDetailResultMapper extends EntityMapper<UserStorageDetailResult, UserStorage>{
}
