package com.example.demo.module.map.dto.result;

import lombok.Data;

@Data
public class MapSelectResult extends MapResult{
    private String startTime;
    private String endTime;

    public MapSelectResult(MapResult mapResult, String startTime, String endTime) {
        super(mapResult.getDutyAddr(), mapResult.getDutyName(), mapResult.getDutyTel1(), mapResult.getHpid(), mapResult.getWgs84Lat(), mapResult.getWgs84Lon());
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
