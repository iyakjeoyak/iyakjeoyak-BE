package com.example.demo.module.medicine.controller;

import com.example.demo.module.bookmark.service.BookmarkService;
import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.heart_medicine.service.HeartMedicineService;
import com.example.demo.module.medicine.dto.payload.MedicineOrderField;
import com.example.demo.module.medicine.dto.payload.MedicinePayload;
import com.example.demo.module.medicine.dto.payload.MedicineSearchCond;
import com.example.demo.module.medicine.dto.payload.OrderSortCond;
import com.example.demo.module.medicine.dto.result.MedicineOfWeekResult;
import com.example.demo.module.medicine.dto.result.MedicineResult;
import com.example.demo.module.medicine.dto.result.MedicineSimpleResult;
import com.example.demo.module.medicine.service.MedicineService;
import com.querydsl.core.types.Order;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "(영양제)", description = "영양제 관련")
@RequestMapping("/medicine")
public class MedicineController {
    private final MedicineService medicineService;
    private final BookmarkService bookmarkService;
    private final HeartMedicineService heartMedicineService;

    @GetMapping("/query")
    @Operation(summary = "필터 기능",
            description =
                    "## 필터 정의 \n" +
                            "\n__categoryId__ : 카테고리 PK | " +
                            "\n__hashtagId__ : 해쉬태그 PK | __keyword__ : 회사명, 영양제 이름  " +
                            "\n### 정렬 관련" +
                            "\n__orderField__ : 정렬 필드명 enum 타입 __[GRADE , HEART_COUNT , CREATED_DATE]__  " +
                            "\n__sort__: 졍렬 기준 enum 타입 [오름차순 __ASC__ , 내림차순 __DESC__]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PageResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<PageResult<MedicineSimpleResult>> findAllByQuery(
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "hashtagId", required = false) Long hashtagId,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "orderBy", required = false) MedicineOrderField medicineOrderField,
            @RequestParam(name = "sort", required = false) Order sort,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @AuthenticationPrincipal Long userId) {
        MedicineSearchCond medicineSearchCond = MedicineSearchCond.builder()
                .categoryId(categoryId)
                .hashtagId(hashtagId)
                .keyword(keyword)
                .orderSortCond(OrderSortCond.builder().medicineOrderField(medicineOrderField).sort(sort).build())
                .build();

        PageResult<MedicineSimpleResult> pageResult = medicineService.findAllByQuery(medicineSearchCond, PageRequest.of(page, size));
        if (userId != null && userId != 0L) {
            pageResult.getData().forEach(d -> {
                d.setIsBookMark(bookmarkService.isChecked(d.getId(), userId));
                d.setIsHeart(heartMedicineService.isChecked(d.getId(), userId));
            });
        }
        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "영양제 전체 조회(페이지네이션)", description = "필터 없이 전체 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PageResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<PageResult<MedicineSimpleResult>> findAll(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                    @RequestParam(name = "size", defaultValue = "10") int size) {
        return new ResponseEntity<>(medicineService.findAll(PageRequest.of(page, size)), HttpStatus.OK);
    }

    @GetMapping("/{medicineId}")
    @Operation(summary = "영양제 단건 조회", description = "medicineId : 영양제 PK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = MedicineResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<MedicineResult> findOneById(@PathVariable(name = "medicineId") Long medicineId, @AuthenticationPrincipal Long userId) {
        Boolean isHeart = false;
        Boolean isBookmark = false;
        System.out.println("userId = " + userId);
        if (userId != null && userId != 0L) {
            isBookmark = bookmarkService.isChecked(medicineId, userId);
            isHeart = heartMedicineService.isChecked(medicineId, userId);
        }
        MedicineResult oneById = medicineService.findOneById(medicineId);
        oneById.setIsBookMark(isBookmark);
        oneById.setIsHeart(isHeart);
        return new ResponseEntity<>(oneById, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "영양제 생성", description = "영양제 DB 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = MedicineResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<MedicineResult> createMedicine(@RequestBody MedicinePayload medicinePayload) {
        long id = medicineService.save(medicinePayload);
        MedicineResult result = medicineService.findOneById(id);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/md")
    @Operation(summary = "광고 영양제 전체 조회(리스트)", description = "광고 영양제 전체 조회(리스트)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<PageResult<MedicineSimpleResult>> findAllByIsAd(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "5", required = false) int size) {
        Sort orderBy = Sort.by(Sort.Direction.DESC, "modifiedDate");
        return new ResponseEntity<>(medicineService.findAllByIsAd(PageRequest.of(page, size, orderBy)), HttpStatus.OK);
    }

    @GetMapping("/week")
    @Operation(summary = "이주의 영양제 조회", description = "이주의 영양제 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<List<MedicineOfWeekResult>> getMedicineOfWeek() {
        return new ResponseEntity<>(medicineService.getMedicineOfWeek(), HttpStatus.OK);
    }

    @GetMapping("/recommend")
    @Operation(summary = "유저 맞춤 추천", description = "유저 맞춤 추천")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<List<MedicineResult>> getRecommend(@AuthenticationPrincipal Long userId, @RequestParam("size") Integer size) {
        return new ResponseEntity<>(medicineService.getRecommend(userId,size), HttpStatus.OK);
    }
}
