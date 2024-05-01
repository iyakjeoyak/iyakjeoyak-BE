package com.example.demo.module.map.controller;

import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.map.dto.result.MapSelectResult;
import com.example.demo.module.map.service.MapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapController {
    private final MapService mapService;

    @GetMapping
    @Operation(summary = "지도 위치 기반 약국 조회", description = "지도 위치 기반 약국 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<PageResult<MapSelectResult>> findPageByLocation(
            @RequestParam(name = "size", defaultValue = "20", required = false) int size,
            @RequestParam(name = "lon") String lon,
            @RequestParam(name = "lat") String lat) throws JSONException, IOException, ParseException {
        return ResponseEntity.status(HttpStatus.OK).body(mapService.findByLocation(lon,lat,size));
    }
}
