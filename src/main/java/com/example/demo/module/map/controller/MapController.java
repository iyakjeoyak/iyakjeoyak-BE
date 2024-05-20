package com.example.demo.module.map.controller;

import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.map.dto.result.MapDetailResult;
import com.example.demo.module.map.dto.result.MapResult;
import com.example.demo.module.map.dto.result.MapSelectResult;
import com.example.demo.module.map.service.MapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/maps")
@RequiredArgsConstructor
@Tag(name = "(지도)", description = "지도 API")
public class MapController {
    private final MapService mapService;

    @GetMapping
    @Operation(summary = "지도 위치 기반 약국 조회", description = "지도 위치 기반 약국 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PageResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<PageResult<MapSelectResult>> findPageByLocation(
            @RequestParam(name = "size", defaultValue = "20", required = false) int size,
            @RequestParam(name = "lon") String lon,
            @RequestParam(name = "lat") String lat,
            @AuthenticationPrincipal Long userId) throws IOException, JSONException {
        return ResponseEntity.status(HttpStatus.OK).body(mapService.findByLocation(lon, lat, size, userId));
    }

    @GetMapping("/{HPID}")
    @Operation(summary = "HPID로 약국 단일 조회", description = "HPID로 약국 단일 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = MapDetailResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<MapDetailResult> findOneByHPID(
            @PathVariable(name = "HPID") String hpid,
            @AuthenticationPrincipal Long userId) throws IOException, JSONException {
        return ResponseEntity.status(HttpStatus.OK).body(mapService.getMapDetail(hpid, userId));
    }

    @GetMapping("/name")
    @Operation(summary = "이름으로 약국 단일 조회", description = "이름으로 약국 단일 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PageResult.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<PageResult<MapDetailResult>> findAllByName(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "district", required = false) String district,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "lon", defaultValue = "127.048878", required = false) Double lon,
            @RequestParam(name = "lat", defaultValue = "37.503737", required = false) Double lat,
            @AuthenticationPrincipal Long userId) throws IOException, JSONException {
        return ResponseEntity.status(HttpStatus.OK).body(mapService.findByNameSortByLocation(name, city, district, lon, lat, size, userId));
    }
}
