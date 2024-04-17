package com.example.demo.util.mapper;

import com.example.demo.domain.entity.Hashtag;
import com.example.demo.web.result.HashtagResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HashtagResultMapper extends EntityMapper<HashtagResult,Hashtag>{
}
