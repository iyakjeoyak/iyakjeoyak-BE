package com.example.demo.module.top_user.controller;

import com.example.demo.module.top_user.service.TopUserService;
import com.example.demo.module.user.dto.result.UserResult;
import com.example.demo.module.user.dto.result.UserSimpleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "(명예의 전당)", description = "명예의 전당 관련")
@RequestMapping("/top-users")
public class TopUserController {
    private final TopUserService topUserService;

    @GetMapping
    @Operation(summary = "명예의 전당 회원 조회", description = "명예의 전당 회원 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<List<UserSimpleResult>> getTopUsers() {
        return new ResponseEntity<>(topUserService.getTopUsers(), HttpStatus.OK);
    }
}