package com.example.demo.module.heart_medicine.controller;

import com.example.demo.module.heart_medicine.service.HeartMedicineService;
import com.example.demo.module.heart_medicine.dto.result.HeartMedicineResult;
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
@Tag(name = "(영양제-좋아요)", description = "영양제 좋아요")
@RequestMapping("/heart/medicine")
public class HeartMedicineController {
    private final HeartMedicineService heartMedicineService;

    @PostMapping
    @Operation(summary = "영양제 좋아요", description = "medicineId : 영양제 PK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> saveHeartMedicine(@RequestBody Long medicineId, /*@AuthenticationPrincipal Long userId*/@RequestParam Long userId){
        return new ResponseEntity<>(heartMedicineService.like(medicineId, userId), HttpStatus.OK);
//        return new ResponseEntity<>(heartMedicineService.like(medicineId, userId), HttpStatus.CREATED);
    }

    @DeleteMapping("/{medicineId}")
    @Operation(summary = "영양제 좋아요 취소", description = "medicineId : 영양제 PK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> deleteHeartMedicine(@PathVariable("medicineId") Long medicineId, /*@AuthenticationPrincipal Long userId*/ @RequestParam("userId") Long userId){
//        return new ResponseEntity<>(heartMedicineService.cancel(medicineId, userId), HttpStatus.OK);
        return new ResponseEntity<>(heartMedicineService.cancel(medicineId, userId), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "영양제 좋아요 전체 조회", description = "유저의 영양제 좋아요 전체 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PageResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<PageResult<HeartMedicineResult>> findAllByUser(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size,/*@AuthenticationPrincipal Long userId*/@RequestParam("userId") Long userId){
        return new ResponseEntity<>(heartMedicineService.findAll(userId, PageRequest.of(page, size)), HttpStatus.OK);
    }

    @GetMapping("/{medicineId}")
    @Operation(summary = "영양제 좋아요 클릭 여부 확인", description = "눌렀으면 true, 안눌렀을땐 false")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Boolean> checkHeartMedicine(@PathVariable("medicineId") Long medicineId, /*@AuthenticationPrincipal Long userId*/@RequestParam("userId") Long userId){
        return new ResponseEntity<>(heartMedicineService.isChecked(medicineId, userId), HttpStatus.OK);
    }
}
