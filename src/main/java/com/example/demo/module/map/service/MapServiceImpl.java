package com.example.demo.module.map.service;

import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.map.dto.result.BusinessHours;
import com.example.demo.module.map.dto.result.MapDetailResult;
import com.example.demo.module.map.dto.result.MapResult;
import com.example.demo.module.map.dto.result.MapSelectResult;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MapServiceImpl implements MapService {

    private final String serviceKey;
    private String serviceUrl = "http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService";

    public MapServiceImpl(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    //    @Value("${open_api.service_key.ermct}")
//    private String serviceKey;

    @Override
    public PageResult<MapSelectResult> findByLocation(String lon, String lat, int size) throws IOException, JSONException {
        StringBuilder sb = new StringBuilder(serviceUrl + "/getParmacyLcinfoInqire");
        sb.append("?").append("serviceKey").append("=").append(serviceKey);
        sb.append("&").append("WGS84_LON").append("=").append(lon);
        sb.append("&").append("WGS84_LAT").append("=").append(lat);
        sb.append("&").append("numOfRows").append("=").append(size);
        sb.append("&").append("_type").append("=").append("json");

//        URL url = new URL(sb.toString());
////
////        BufferedReader br;
////        br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
////        String result = br.readLine();
////        JSONParser jsonParser = new JSONParser();
////        JSONObject jsonObject;
////
////        try{
////            jsonObject = (JSONObject) jsonParser.parse(result);
////        }catch (Exception e) {
////            throw new JSONException("파싱 실패");
////        }
////
////        JSONObject response = (JSONObject) jsonObject.get("response");
////        JSONObject body = (JSONObject) response.get("body");

        JSONObject body = getBodyValue(sb.toString());
        JSONObject items = (JSONObject) body.get("items");
        JSONArray array = (JSONArray) items.get("item");

        List<MapSelectResult> list = new ArrayList<>();
        for (Object o : array) {
            JSONObject i = (JSONObject) o;
            list.add(new MapSelectResult(getMapSelectResult(i), i.get("startTime").toString(), i.get("endTime").toString()));
        }

        PageResult<MapSelectResult> pageResult = new PageResult<>();
        pageResult.setData(list);
        pageResult.setNumber(Integer.parseInt(body.get("pageNo").toString()));
        pageResult.setSize(Integer.parseInt(body.get("numOfRows").toString()));
        pageResult.setTotalElement(Long.parseLong(body.get("totalCount").toString()));
        return pageResult;
    }

    @Override
    public MapDetailResult getMapDetail(String mapId) throws IOException, JSONException {
        StringBuilder sb = new StringBuilder(serviceUrl + "/getParmacyBassInfoInqire");
        sb.append("?").append("serviceKey").append("=").append(serviceKey);
        sb.append("&").append("HPID").append("=").append(mapId);
        sb.append("&").append("_type").append("=").append("json");

        JSONObject body = getBodyValue(sb.toString());
        JSONObject items = (JSONObject) body.get("items");
        JSONObject item = (JSONObject) items.get("item");


        return new MapDetailResult(getMapSelectResult(item), getBusinessHoursList(item));
    }

    // 내무 메서드
    private JSONObject getBodyValue(String json) throws IOException, JSONException {
        URL url = new URL(json);

        BufferedReader br;
        br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        String result = br.readLine();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(result);
        } catch (Exception e) {
            throw new JSONException("파싱 실패");
        }

        JSONObject response = (JSONObject) jsonObject.get("response");
        return (JSONObject) response.get("body");
    }

    private MapResult getMapSelectResult(JSONObject i) {
        String wgs84Lon = i.get("wgs84Lon") == null ? i.get("longitude").toString() : i.get("wgs84Lon").toString();
        String wgs84Lat = i.get("wgs84Lat") == null ? i.get("latitude").toString() : i.get("wgs84Lat").toString();
        return MapResult.builder()
                .dutyAddr((String) i.get("dutyAddr"))
                .dutyName((String) i.get("dutyName"))
                .dutyTel1((String) i.get("dutyTel1"))
                .wgs84Lon(Double.parseDouble(wgs84Lon))
                .wgs84Lat(Double.parseDouble(wgs84Lat))
                .hpid((String) i.get("hpid"))
                .build();
    }

    private List<BusinessHours> getBusinessHoursList(JSONObject i) {
        List<BusinessHours> businessHours = new ArrayList<>();
        for (int n = 1; n <= 8; n++) {
            String str = "dutyTime";
            businessHours.add(
                    BusinessHours.builder()
                            .dayOfWeek(getDayOfWeek(n))
                            .startHour(i.get(str + n + "s").toString())
                            .endHour(i.get(str + n + "c").toString())
                            .build());
        }
        return businessHours;
    }

    private String getDayOfWeek(int n) {
        return switch (n) {
            case 1 -> "Mon";
            case 2 -> "Tue";
            case 3 -> "Wed";
            case 4 -> "Thu";
            case 5 -> "Fri";
            case 6 -> "Sat";
            case 7 -> "Sun";
            case 8 -> "Off";
            default -> "";
        };
    }
}
