package com.example.demo.util.mapper;

import com.example.demo.domain.entity.UserStorage;
import com.example.demo.web.result.UserStorageSimpleResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserStorageSimpleResultMapper extends EntityMapper<UserStorageSimpleResult , UserStorage> {
}
