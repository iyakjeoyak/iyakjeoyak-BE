package com.example.demo.util.mapper;

import com.example.demo.module.user.dto.result.UserSimpleResult;
import com.example.demo.module.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserSimpleResultMapper extends EntityMapper<UserSimpleResult, User> {
}
