package com.example.demo.module.map.dto.result;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MapDetailResult extends MapResult {
    private List<BusinessHours> businessHoursList = new ArrayList<>();

    public MapDetailResult(MapResult mapSelectResult, List<BusinessHours> businessHoursList) {
        super(mapSelectResult.getDutyAddr(),
                mapSelectResult.getDutyName(),
                mapSelectResult.getDutyTel1(),
                mapSelectResult.getHpid(),
                mapSelectResult.getWgs84Lat(),
                mapSelectResult.getWgs84Lon());
        this.businessHoursList = businessHoursList;
    }
}
