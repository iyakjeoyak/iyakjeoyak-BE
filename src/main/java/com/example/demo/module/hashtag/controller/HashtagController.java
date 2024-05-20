package com.example.demo.module.hashtag.controller;

import com.example.demo.module.hashtag.service.HashtagService;
import com.example.demo.module.hashtag.dto.result.HashtagResult;
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
@Tag(name = "(해쉬태그)", description = "해쉬태그 관련")
@RequestMapping("/hashtags")
public class HashtagController {
    private final HashtagService hashtagService;

    @GetMapping("")
    @Operation(summary = "해쉬태그 전체 조회", description = "해쉬태그 전체 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<List<HashtagResult>> getCountByReviewId() {
        return new ResponseEntity<>(hashtagService.findAll(), HttpStatus.OK);
    }
}
