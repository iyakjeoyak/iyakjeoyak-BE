package com.example.demo.util.mapper;

import com.example.demo.module.user.dto.result.UserResult;
import com.example.demo.module.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserResult, User>{
}
