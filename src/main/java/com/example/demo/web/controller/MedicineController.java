package com.example.demo.web.controller;

import com.example.demo.service.MedicineService;
import com.example.demo.web.result.MedicineResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("medicine")
public class MedicineController {
    private final MedicineService medicineService;


    @GetMapping()
    @Operation(summary = "영양제 전체 조회", description = "영양제 전체 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = List.class)))})
    public ResponseEntity<List<MedicineResult>> findAll() {
        return new ResponseEntity<>(medicineService.findAll(), HttpStatus.OK);
    }
}
