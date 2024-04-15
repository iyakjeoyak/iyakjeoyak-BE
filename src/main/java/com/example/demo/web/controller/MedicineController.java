package com.example.demo.web.controller;

import com.example.demo.service.MedicineService;
import com.example.demo.web.result.MedicineResult;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "(영양제)", description = "영양제 관련")
@RequestMapping("medicine")
public class MedicineController {
    private final MedicineService medicineService;


    @GetMapping()
    @Operation(summary = "영양제 전체 조회", description = "이후 페이지네이션으로 바꿀 예정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = List.class)))})
    public ResponseEntity<List<MedicineResult>> findAll() {
        return new ResponseEntity<>(medicineService.findAll(), HttpStatus.OK);
    }
    @GetMapping("/{medicineId}")
    @Operation(summary = "영양제 단건 조회", description = "medicineId : 영양제 PK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = MedicineResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = MedicineResult.class)))})
    public ResponseEntity<MedicineResult> findOneById(@PathVariable(name = "medicineId") Long medicineId) {
        return new ResponseEntity<>(medicineService.findOneById(medicineId), HttpStatus.OK);
    }
}
