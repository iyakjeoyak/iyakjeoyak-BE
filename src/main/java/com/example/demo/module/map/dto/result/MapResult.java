package com.example.demo.module.map.dto.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MapResult {
    private String dutyAddr;
    private String dutyName;
    private String dutyTel1;
    private String hpid;
    private Double latitude;
    private Double longitude;
    private Boolean liked;

}
