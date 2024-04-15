package com.example.demo.web.controller;

import com.example.demo.domain.entity.HeartMedicine;
import com.example.demo.service.HeartMedicineService;
import com.example.demo.web.result.HeartMedicineResult;
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
@Tag(name = "(좋아요)", description = "영양제 좋아요")
@RequestMapping("/heart/medicine")
public class HeartMedicineController {
    private final HeartMedicineService heartMedicineService;

    @PostMapping("/{medicineId}")
    @Operation(summary = "영양제 좋아요", description = "medicineId : 영양제 PK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> likeMedicine(@PathVariable Long medicineId){
        return new ResponseEntity<>(heartMedicineService.like(medicineId), HttpStatus.CREATED);
    }

    @DeleteMapping("/{medicineId}")
    @Operation(summary = "영양제 좋아요 취소", description = "medicineId : 영양제 PK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Long> likeMedicineCancel(@PathVariable Long medicineId){
        return new ResponseEntity<>(heartMedicineService.cancel(medicineId), HttpStatus.OK);
    }

    @GetMapping@Operation(summary = "영양제 좋아요 전체 조회", description = "유저의 영양제 좋아요 전체 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<List<HeartMedicineResult>> findAll(){
        return new ResponseEntity<>(heartMedicineService.findAll(), HttpStatus.OK);
    }
}
