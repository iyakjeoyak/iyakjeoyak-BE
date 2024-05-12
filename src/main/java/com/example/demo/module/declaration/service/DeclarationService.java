package com.example.demo.module.declaration.service;

import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.declaration.dto.payload.DeclarationPayload;
import com.example.demo.module.declaration.dto.result.DeclarationResult;
import org.springframework.data.domain.PageRequest;

public interface DeclarationService {
    Long save(DeclarationPayload declarationPayload, Long userId);

    Long delete(Long declarationId, Long userId);

    PageResult<DeclarationResult> findAll(Long userId, PageRequest pageRequest);

    DeclarationResult findOneByUser(Long declarationId, Long userId);
}
