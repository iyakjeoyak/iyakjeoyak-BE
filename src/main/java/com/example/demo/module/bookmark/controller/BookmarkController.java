package com.example.demo.module.bookmark.controller;

import com.example.demo.module.bookmark.service.BookmarkService;
import com.example.demo.module.bookmark.dto.result.BookmarkResult;
import com.example.demo.module.common.result.PageResult;
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
@Tag(name = "북마크", description = "북마크 관련")
@RequestMapping("/bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @GetMapping
    @Operation(summary = "북마크 전체 조회", description = "유저의 북마크 전체 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PageResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<PageResult<BookmarkResult>> findAllByUser(@RequestParam(defaultValue = "0", name = "page") int page,
                                                                    @RequestParam(defaultValue = "10", name = "size") int size,
                                                                    @AuthenticationPrincipal Long userId){
        return new ResponseEntity<>(bookmarkService.findAll(userId, PageRequest.of(page, size)), HttpStatus.OK);
    }

    @GetMapping("/{bookmarkId}")
    @Operation(summary = "북마크 단일 조회", description = "북마크 단일 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = BookmarkResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<BookmarkResult> findOneByUser(@PathVariable("bookmarkId") Long bookmarkId, @AuthenticationPrincipal Long userId){
        return new ResponseEntity<>(bookmarkService.findOneByUser(bookmarkId, userId), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "북마크 생성", description = "북마크 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> save(@RequestBody Long medicineId, @AuthenticationPrincipal Long userId){
        return new ResponseEntity<>(bookmarkService.save(medicineId, userId), HttpStatus.CREATED);
    }

    @DeleteMapping("/{medicineId}")
    @Operation(summary = "북마크 삭제", description = "북마크 취소")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> delete(@PathVariable("medicineId") Long medicineId, @AuthenticationPrincipal Long userId){
        return new ResponseEntity<>(bookmarkService.delete(medicineId, userId), HttpStatus.OK);
    }

    @GetMapping("/medicine/{medicineId}")
    @Operation(summary = "북마크 클릭 여부 확인", description = "눌렀으면 true, 안눌렀을땐 false")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Boolean> checkBookmark(@PathVariable("medicineId") Long medicineId, @AuthenticationPrincipal Long userId){
        return new ResponseEntity<>(bookmarkService.isChecked(medicineId, userId), HttpStatus.OK);
    }

    @PostMapping("/click")
    @Operation(summary = "북마크 생성/삭제", description = "북마크 클릭하면 tre / 취소하면 false 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "북마크 생성", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "200", description = "북마크 삭제", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Boolean> bookmarkClick(@RequestBody Long medicineId, @AuthenticationPrincipal Long userId){
        if (bookmarkService.click(medicineId, userId)){
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        } return new ResponseEntity<>(false, HttpStatus.OK);
    }
}
