package com.example.demo.web.controller;

import com.example.demo.service.UserStorageService;
import com.example.demo.web.payload.UserStorageCreatePayload;
import com.example.demo.web.payload.UserStorageEditPayload;
import com.example.demo.web.result.CategoryResult;
import com.example.demo.web.result.PageResult;
import com.example.demo.web.result.UserStorageDetailResult;
import com.example.demo.web.result.UserStorageSimpleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "(영양제 보관함)", description = "보관함 관련")
@RequestMapping("/storage")
public class UserStorageController {
    private final UserStorageService userStorageService;

    @GetMapping("")
    @Operation(summary = "유저 보관함 전체 조회", description = "유저 보관함 전체 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserStorageSimpleResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<PageResult<UserStorageSimpleResult>> getAllByUserId(@RequestParam("userId") Long userId, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        return new ResponseEntity<>(userStorageService.getAllByUserId(userId, page, size), HttpStatus.OK);
    }

    @GetMapping("/{storageId}")
    @Operation(summary = "유저 보관함 단일 조회", description = "유저 보관함 단일 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserStorageDetailResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<UserStorageDetailResult> getOneById(@PathVariable("storageId") Long storageId) {
        return new ResponseEntity<>(userStorageService.getOneById(storageId), HttpStatus.OK);
    }

    @PostMapping("")
    @Operation(summary = "유저 보관함 생성", description = "유저 보관함 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserStorageSimpleResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> saveStorage(@RequestParam("userId") Long userId, @RequestBody UserStorageCreatePayload payload) {
        return new ResponseEntity<>(userStorageService.saveUserStorage(userId, payload), HttpStatus.CREATED);
    }

    @PatchMapping("/{storageId}")
    @Operation(summary = "유저 보관함 수정", description = "유저 보관함 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserStorageSimpleResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> saveStorage(@PathVariable("storageId") Long storageId, @RequestParam("userId") Long userId, @RequestBody UserStorageEditPayload payload) {
        return new ResponseEntity<>(userStorageService.editUserStorage(userId, storageId, payload), HttpStatus.CREATED);
    }

    @DeleteMapping("/{storageId}")
    @Operation(summary = "유저 보관함 삭제", description = "유저 보관함 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> deleteById(@PathVariable("storageId") Long storageId) {
        return new ResponseEntity<>(userStorageService.deleteById(storageId), HttpStatus.OK);
    }
}
