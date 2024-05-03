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
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class MapServiceImpl implements MapService {

    private final String serviceKey;
    private String serviceUrl = "http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService";

    public MapServiceImpl(String serviceKey) {
        this.serviceKey = serviceKey;
    }


    @Override
    public PageResult<MapSelectResult> findByLocation(String lon, String lat, int size) throws IOException, JSONException {
        StringBuilder sb = new StringBuilder(serviceUrl + "/getParmacyLcinfoInqire");
        sb.append("?").append("serviceKey").append("=").append(serviceKey);
        sb.append("&").append("WGS84_LON").append("=").append(lon);
        sb.append("&").append("WGS84_LAT").append("=").append(lat);
        sb.append("&").append("numOfRows").append("=").append(size);
        sb.append("&").append("_type").append("=").append("json");

        return getMapSelectResultPageResult(sb.toString());
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

    @Override
    public PageResult<MapDetailResult> findByNameSortByLocation(String name, String city, String district, Double lon, Double lat, int size) throws IOException, JSONException {
        StringBuilder sb = new StringBuilder(serviceUrl + "/getParmacyListInfoInqire");
        sb.append("?").append("serviceKey").append("=").append(serviceKey);
        if (StringUtils.hasText(name)) {
            sb.append("&").append("QN").append("=").append(URLEncoder.encode(name, "UTF-8"));
        }
        if (StringUtils.hasText(district)) {
            sb.append("&").append("Q1").append("=").append(URLEncoder.encode(district, "UTF-8"));
        }
        if (StringUtils.hasText(city)) {
            sb.append("&").append("Q0").append("=").append(URLEncoder.encode(city, "UTF-8"));
        }
        sb.append("&").append("numOfRows").append("=").append(size);
        sb.append("&").append("_type").append("=").append("json");

        PageResult<MapDetailResult> mapMapDetailResultPageResult = getMapDetailResultPageResult(sb.toString());
        mapMapDetailResultPageResult.getData().sort(Comparator.comparingDouble(i -> Math.abs(i.getLongitude() - lon) + Math.abs(i.getLatitude() - lat)));

        return mapMapDetailResultPageResult;
    }

    // 내부 메서드
    private PageResult<MapSelectResult> getMapSelectResultPageResult(String result) throws IOException {
        JSONObject body = getBodyValue(result);

        JSONObject items = (JSONObject) body.get("items");
        if (items.toString().isEmpty()) {
            PageResult<MapSelectResult> pageResult = new PageResult<>();
            pageResult.setData(List.of());
            pageResult.setNumber(Integer.parseInt(body.get("pageNo").toString()));
            pageResult.setSize(Integer.parseInt(body.get("numOfRows").toString()));
            pageResult.setTotalElement(Long.parseLong(body.get("totalCount").toString()));
            return pageResult;
        }
        JSONArray array = new JSONArray();
        try {
            array = (JSONArray) items.get("item");
        } catch (ClassCastException e) {
            JSONObject item = (JSONObject) items.get("item");
            if (!item.toString().isEmpty()) {
                array.add(item);
            }
        }

        List<MapSelectResult> list = new ArrayList<>();
        for (Object o : array) {
            JSONObject i = (JSONObject) o;
            String startTime = i.get("startTime") == null ? "" : i.get("startTime").toString();
            String endTime = i.get("endTime") == null ? "" : i.get("endTime").toString();

            list.add(new MapSelectResult(getMapSelectResult(i), startTime, endTime));
        }

        PageResult<MapSelectResult> pageResult = new PageResult<>();
        pageResult.setData(list);
        pageResult.setNumber(Integer.parseInt(body.get("pageNo").toString()));
        pageResult.setSize(Integer.parseInt(body.get("numOfRows").toString()));
        pageResult.setTotalElement(Long.parseLong(body.get("totalCount").toString()));
        return pageResult;
    }

    private PageResult<MapDetailResult> getMapDetailResultPageResult(String result) throws IOException {
        JSONObject body = getBodyValue(result);

        JSONObject items = (JSONObject) body.get("items");
        if (items.toString().isEmpty()) {
            PageResult<MapDetailResult> pageResult = new PageResult<>();
            pageResult.setData(List.of());
            pageResult.setNumber(Integer.parseInt(body.get("pageNo").toString()));
            pageResult.setSize(Integer.parseInt(body.get("numOfRows").toString()));
            pageResult.setTotalElement(Long.parseLong(body.get("totalCount").toString()));
            return pageResult;
        }
        JSONArray array = new JSONArray();
        try {
            array = (JSONArray) items.get("item");
        } catch (ClassCastException e) {
            JSONObject item = (JSONObject) items.get("item");
            if (!item.toString().isEmpty()) {
                array.add(item);
            }
        }

        List<MapDetailResult> list = new ArrayList<>();
        for (Object o : array) {
            JSONObject i = (JSONObject) o;
            String startTime = i.get("startTime") == null ? "" : i.get("startTime").toString();
            String endTime = i.get("endTime") == null ? "" : i.get("endTime").toString();

            list.add(new MapDetailResult(new MapSelectResult(getMapSelectResult(i), startTime, endTime), getBusinessHoursList(i)));
        }

        PageResult<MapDetailResult> pageResult = new PageResult<>();
        pageResult.setData(list);
        pageResult.setNumber(Integer.parseInt(body.get("pageNo").toString()));
        pageResult.setSize(Integer.parseInt(body.get("numOfRows").toString()));
        pageResult.setTotalElement(Long.parseLong(body.get("totalCount").toString()));
        return pageResult;
    }

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
                .longitude(Double.parseDouble(wgs84Lon))
                .latitude(Double.parseDouble(wgs84Lat))
                .hpid((String) i.get("hpid"))
                .build();
    }

    private List<BusinessHours> getBusinessHoursList(JSONObject i) {
        List<BusinessHours> businessHours = new ArrayList<>();
        for (int n = 1; n <= 8; n++) {
            String str = "dutyTime";
            Object start = i.get(str + n + "s");
            Object close = i.get(str + n + "c");
            businessHours.add(
                    BusinessHours.builder()
                            .dayOfWeek(getDayOfWeek(n))
                            .startHour(start == null ? "off" : start.toString())
                            .endHour(close == null ? "off" : close.toString())
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
            case 8 -> "Hol";
            default -> "";
        };
    }
}