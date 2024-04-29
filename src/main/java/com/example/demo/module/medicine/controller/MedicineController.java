package com.example.demo.module.medicine.controller;

import com.example.demo.module.medicine.dto.payload.MedicineSearchCond;
import com.example.demo.module.medicine.service.MedicineService;
import com.example.demo.module.medicine.dto.payload.MedicinePayload;
import com.example.demo.module.medicine.dto.result.MedicineResult;
import com.example.demo.module.medicine.dto.result.MedicineSimpleResult;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "(영양제)", description = "영양제 관련")
@RequestMapping("/medicine")
public class MedicineController {
    private final MedicineService medicineService;

    @PostMapping("/query")
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
            @RequestBody MedicineSearchCond searchCond,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        return new ResponseEntity<>(medicineService.findAllByQuery(searchCond, PageRequest.of(page, size)), HttpStatus.OK);
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
    public ResponseEntity<MedicineResult> findOneById(@PathVariable(name = "medicineId") Long medicineId) {
        return new ResponseEntity<>(medicineService.findOneById(medicineId), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "영양제 생성", description = "영양제 DB 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = MedicineResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<MedicineResult> createMedicine(@RequestBody MedicinePayload medicinePayload) {
        long id = medicineService.save(medicinePayload);
        MedicineResult medicineResult = medicineService.findOneById(id);
        return new ResponseEntity<>(medicineResult, HttpStatus.CREATED);
    }
}
