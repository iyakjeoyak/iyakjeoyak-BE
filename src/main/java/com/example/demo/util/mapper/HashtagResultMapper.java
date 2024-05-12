package com.example.demo.util.mapper;

import com.example.demo.module.hashtag.entity.Hashtag;
import com.example.demo.module.hashtag.dto.result.HashtagResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HashtagResultMapper extends EntityMapper<HashtagResult,Hashtag>{
}
