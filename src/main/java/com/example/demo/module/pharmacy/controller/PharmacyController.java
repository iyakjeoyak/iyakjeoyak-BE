package com.example.demo.module.pharmacy.controller;

import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.pharmacy.dto.payload.PharmacyPayload;
import com.example.demo.module.pharmacy.dto.result.PharmacyResult;
import com.example.demo.module.pharmacy.service.PharmacyService;
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
@Tag(name = "지도(약국)", description = "약국 지도 관련")
@RequestMapping("/pharmacies")
public class PharmacyController {
    private final PharmacyService pharmacyService;
    @GetMapping
    @Operation(summary = "유저의 약국 전체 조회", description = "유저가 저장한 약국 전체 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PageResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<PageResult<PharmacyResult>> getAllByUserId(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                     @RequestParam(name = "size", defaultValue = "10") int size,
                                                                     @AuthenticationPrincipal Long userId) {
        return new ResponseEntity<>(pharmacyService.getAllByUserId(userId, PageRequest.of(page, size)), HttpStatus.OK);
    }

    @GetMapping("/{pharmacyId}")
    @Operation(summary = "유저 약국 단일 조회", description = "유저가 저장한 약국 단일 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PharmacyResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<PharmacyResult> getOneById(@PathVariable("pharmacyId") Long pharmacyId, @AuthenticationPrincipal Long userId) {
        return new ResponseEntity<>(pharmacyService.getOneById(pharmacyId, userId), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "약국 저장", description = "약국 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> save(@RequestBody PharmacyPayload pharmacyPayload, @AuthenticationPrincipal Long userId) {
        return new ResponseEntity<>(pharmacyService.save(userId, pharmacyPayload), HttpStatus.CREATED);
    }


    @DeleteMapping("/{pharmacyId}")
    @Operation(summary = "약국 삭제", description = "약국 저장 취소")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> deleteById(@PathVariable("pharmacyId") Long pharmacyId, @AuthenticationPrincipal Long userId) {
        return new ResponseEntity<>(pharmacyService.delete(userId, pharmacyId), HttpStatus.OK);
    }
}
