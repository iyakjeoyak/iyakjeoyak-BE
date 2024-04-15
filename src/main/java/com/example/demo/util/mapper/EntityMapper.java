package com.example.demo.util.mapper;

import java.util.List;

/**
 * packageName    : thetak.platform.api._core.common.utils.mapstruct
 * fileName       : EntityMapper
 * author         : Wonhyeok Hwang
 * date           : 2023/10/19
 * description    :
 */
public interface EntityMapper<D, E>{
    D toDto(final E entity);
    List<D> toDtoList(final List<E> entities);
}