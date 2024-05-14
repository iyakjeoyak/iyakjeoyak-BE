package com.example.demo.module.declaration.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.declaration.dto.payload.DeclarationPayload;
import com.example.demo.module.declaration.dto.result.DeclarationResult;
import com.example.demo.module.declaration.entity.Declaration;
import com.example.demo.module.declaration.repository.DeclarationRepository;
import com.example.demo.module.review.repository.ReviewRepository;
import com.example.demo.module.user.repository.UserRepository;
import com.example.demo.util.mapper.ReviewMapper;
import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeclarationServiceImpl implements DeclarationService{
    private final DeclarationRepository declarationRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    @Override
    public PageResult<DeclarationResult> findAll(Long userId, PageRequest pageRequest) {
        Page<DeclarationResult> declarations = declarationRepository.findAllByUserUserId(userId, pageRequest)
                .map(declaration -> declaration.toDto(reviewMapper));
        return new PageResult<>(declarations);
    }

    @Override
    public DeclarationResult findOneByUser(Long declarationId, Long userId) {
        return declarationRepository.findByIdAndUserUserId(declarationId, userId)
                    .orElseThrow(() -> new CustomException(DECLARATION_NOT_FOUND)).toDto(reviewMapper);
    }

    @Counted("my.declaration")
    @Override
    @Transactional
    public Long save(DeclarationPayload declarationPayload, Long userId) {
        if(!declarationRepository.existsByReviewIdAndUserUserId(declarationPayload.getReviewId(), userId)) {
            return declarationRepository.save(Declaration
                    .builder()
                    .user(userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND)))
                    .review(reviewRepository.findById(declarationPayload.getReviewId()).orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND)))
                    .title(declarationPayload.getTitle())
                    .content(declarationPayload.getContent())
                    .build()).getId();
        }
        throw new CustomException(DECLARATION_DUPLICATION);
    }

    @Override
    @Transactional
    public Long delete(Long declarationId, Long userId) {
        if(declarationRepository.existsByIdAndUserUserId(declarationId, userId)){
            declarationRepository.deleteById(declarationId);
            return declarationId;
        }
        throw new CustomException(DECLARATION_NOT_FOUND);
    }
}
