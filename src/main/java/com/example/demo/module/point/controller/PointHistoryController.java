package com.example.demo.module.point.controller;

import com.example.demo.module.common.result.PageResult;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<PageResult<PointHistoryResult>> findAllByUser(@RequestParam(defaultValue = "0", name = "page") int page,
                                                                        @RequestParam(defaultValue = "10", name = "size") int size,
                                                                        @RequestParam("userId") Long userId){
        return new ResponseEntity<>(pointService.findAllByUserId(userId, PageRequest.of(page, size)), HttpStatus.OK);
    }
}
