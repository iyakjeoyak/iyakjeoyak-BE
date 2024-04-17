package com.example.demo.module.medicine.controller;

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

    @GetMapping
    @Operation(summary = "영양제 전체 조회", description = "이후 페이지네이션으로 바꿀 예정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PageResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<PageResult<MedicineSimpleResult>> findAll(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                    @RequestParam(name = "size",defaultValue = "10") int size) {
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
    public ResponseEntity<MedicineResult> createMedicine(@RequestBody MedicinePayload medicinePayload){
        long id = medicineService.save(medicinePayload);
        MedicineResult medicineResult = medicineService.findOneById(id);
        return new ResponseEntity<>(medicineResult, HttpStatus.CREATED);
    }
}
