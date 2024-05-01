package com.example.demo.module.map.service;

import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.map.dto.result.MapSelectResult;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {
    @Value("${open_api.service_key.ermct}")
    private String serviceKey;

    @Override
    public PageResult<MapSelectResult> findByLocation(String lon, String lat, int size) throws IOException, ParseException {
        StringBuilder sb = new StringBuilder("http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyLcinfoInqire");
        sb.append("?").append("serviceKey").append("=").append(serviceKey);
        sb.append("&").append("WGS84_LON").append("=").append(lon);
        sb.append("&").append("WGS84_LAT").append("=").append(lat);
        sb.append("&").append("numOfRows").append("=").append(size);
        sb.append("&").append("_type").append("=").append("json");

        URL url = new URL(sb.toString());

        BufferedReader br;
        br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        String result = br.readLine();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
        JSONObject response = (JSONObject) jsonObject.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray array = (JSONArray) items.get("item");

        List<MapSelectResult> list = new ArrayList<>();
        for (Object o : array) {
            JSONObject i = (JSONObject) o;
            MapSelectResult build1 = MapSelectResult.builder()
                    .dutyAddr((String) i.get("dutyAddr"))
                    .dutyName((String) i.get("dutyName"))
                    .dutyTel1((String) i.get("dutyTel1"))
                    .hpid((String) i.get("hpid"))
                    .wgs84Lon(Double.parseDouble(i.get("longitude").toString()))
                    .wgs84Lat(Double.parseDouble(i.get("latitude").toString()))
                    .build();
            list.add(build1);
        }

        PageResult<MapSelectResult> pageResult = new PageResult<>();
        pageResult.setData(list);
        pageResult.setNumber(Integer.parseInt(body.get("pageNo").toString()));
        pageResult.setSize(Integer.parseInt(body.get("numOfRows").toString()));
        pageResult.setTotalElement(Long.parseLong(body.get("totalCount").toString()));
        return pageResult;
    }
}
