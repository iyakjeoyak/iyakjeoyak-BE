package com.example.demo.module.point.controller;

import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.point.dto.payload.PointOrderField;
import com.example.demo.module.point.dto.result.PointHistoryResult;
import com.example.demo.module.point.service.PointHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "포인트 내역 관련")
@RequestMapping("/point")
public class PointHistoryController {
    private final PointHistoryService pointService;

    @GetMapping
    @Operation(summary = "유저 포인트 내역 조회", description = "유저 포인트 내역 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PageResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<PageResult<PointHistoryResult>> findAllByUser(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "orderBy", defaultValue = "ID", required = false) PointOrderField pointOrderField,
            @RequestParam(name = "sort", defaultValue = "DESC", required = false) String sort,
            @AuthenticationPrincipal Long userId) {
        Sort orderBy = sort.equals("ASC") ?
                Sort.by(Sort.Direction.ASC, pointOrderField.getValue()) : Sort.by(Sort.Direction.DESC, pointOrderField.getValue());
        return new ResponseEntity<>(pointService.findAllByUserId(userId, PageRequest.of(page, size, orderBy)), HttpStatus.OK);
    }
}
