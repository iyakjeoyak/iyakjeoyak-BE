package com.example.demo.module.map.service;

import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.map.dto.result.MapSelectResult;
import org.json.simple.parser.ParseException;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.io.IOException;

public interface MapService {
    PageResult<MapSelectResult> findByLocation(String lon, String lat, int size) throws IOException, ParseException, JSONException;
}
