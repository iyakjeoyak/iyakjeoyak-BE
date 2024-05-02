package com.example.demo.module.map.service;

import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.map.dto.result.MapDetailResult;
import com.example.demo.module.map.dto.result.MapResult;
import com.example.demo.module.map.dto.result.MapSelectResult;
import org.json.JSONException;

import java.io.IOException;

public interface MapService {
    PageResult<MapSelectResult> findByLocation(String lon, String lat, int size) throws IOException, JSONException;

    MapDetailResult getMapDetail(String mapId) throws IOException, JSONException;
}
