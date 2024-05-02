package com.example.demo.module.map.service;

import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.map.dto.result.MapDetailResult;
import com.example.demo.module.map.dto.result.MapSelectResult;
import org.json.JSONException;

import java.io.IOException;

public interface MapService {
    PageResult<MapSelectResult> findByLocation(String lon, String lat, int size) throws IOException, JSONException;

    MapDetailResult getMapDetail(String mapId) throws IOException, JSONException;

    PageResult<MapDetailResult> findByNameSortByLocation(String name, String city, String district, Double lon, Double lat, int size) throws IOException, JSONException;
}
