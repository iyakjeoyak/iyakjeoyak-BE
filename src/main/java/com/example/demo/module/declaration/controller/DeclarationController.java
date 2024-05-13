package com.example.demo.module.declaration.controller;

import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.declaration.dto.payload.DeclarationPayload;
import com.example.demo.module.declaration.dto.result.DeclarationResult;
import com.example.demo.module.declaration.service.DeclarationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "(신고)", description = "신고하기 관련")
@RequestMapping("/declarations")
public class DeclarationController {
    private final DeclarationService declarationService;

    @GetMapping
    @Operation(summary = "신고 내역 전체 조회", description = "유저의 신고 내역 전체 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PageResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<PageResult<DeclarationResult>> findAllByUser(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size,
                                                                       @AuthenticationPrincipal Long userId){
        return new ResponseEntity<>(declarationService.findAll(userId, PageRequest.of(page, size)), HttpStatus.OK);
    }

    @GetMapping("/{declarationId}")
    @Operation(summary = "신고 단일 조회", description = "유저의 신고 단일 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = DeclarationResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<DeclarationResult> findOneByUser(@PathVariable("declarationId") Long declarationId, @AuthenticationPrincipal Long userId){
        return new ResponseEntity<>(declarationService.findOneByUser(declarationId, userId), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "신고하기", description = "리뷰 신고하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> declareReview(@RequestBody DeclarationPayload declarationPayload, @AuthenticationPrincipal Long userId){
        return new ResponseEntity<>(declarationService.save(declarationPayload, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{declarationId}")
    @Operation(summary = "신고 취소하기", description = "리뷰 신고 취소하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> deleteDeclaration(@PathVariable("declarationId") Long declarationId, @AuthenticationPrincipal Long userId){
        return new ResponseEntity<>(declarationService.delete(declarationId, userId), HttpStatus.OK);
    }

}
