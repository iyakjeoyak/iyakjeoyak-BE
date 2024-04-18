package com.example.demo.module.declaration.service;

import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.declaration.dto.payload.DeclarationPayload;
import com.example.demo.module.declaration.dto.result.DeclarationResult;
import com.example.demo.module.declaration.entity.Declaration;
import com.example.demo.module.declaration.repository.DeclarationRepository;
import com.example.demo.module.review.repository.ReviewRepository;
import com.example.demo.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DeclarationServiceImpl implements DeclarationService{
    private final DeclarationRepository declarationRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    @Override
    public PageResult<DeclarationResult> findAll(Long userId, PageRequest pageRequest) {
        Page<DeclarationResult> declarations = declarationRepository.findAllByUserUserId(userId, pageRequest).map(Declaration::toDto);
        return new PageResult<>(declarations);
    }

    @Override
    public DeclarationResult findOneByUser(Long declarationId, Long userId) {
        if(declarationRepository.existsByIdAndUserUserId(declarationId, userId)){
            return declarationRepository.findById(declarationId)
                    .orElseThrow(() -> new NoSuchElementException("신고내역이 없습니다.")).toDto();
        }
        throw new IllegalArgumentException("해당 유저의 신고내역이 아닙니다.");
    }

    @Override
    public Long save(DeclarationPayload declarationPayload, Long userId) {
        if(!declarationRepository.existsByReviewIdAndUserUserId(declarationPayload.getReviewId(), userId)) {
            return declarationRepository.save(Declaration
                    .builder()
                    .user(userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("해당하는 유저는 없습니다.")))
                    .review(reviewRepository.findById(declarationPayload.getReviewId()).orElseThrow(() -> new NoSuchElementException("해당하는 리뷰는 없습니다.")))
                    .title(declarationPayload.getTitle())
                    .content(declarationPayload.getContent())
                    .build()).getId();
        }
        throw new IllegalArgumentException("이미 신고한 리뷰입니다.");
    }

    @Override
    public Long delete(Long declarationId, Long userId) {
        if(declarationRepository.existsByIdAndUserUserId(declarationId, userId)){
            declarationRepository.deleteById(declarationId);
            return declarationId;
        }
        throw new IllegalArgumentException("해당 유저의 신고내역이 아닙니다.");
    }
}