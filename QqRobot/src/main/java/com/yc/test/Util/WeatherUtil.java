package com.yc.test.Util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @author yucan
 * @date 2022/12/21 11:17
 */
public class WeatherUtil {
    public static String getWeather(String local) {
        String result = "";
        String url = "https://api.seniverse.com/v3/weather/daily.json?key=SV6yK01eFy5jpzfD2&location=" + local + "&language=zh-Hans&unit=c&start=0&days=5";
        String weatherResult = HttpRequestUtil.doGet(url);
        JSONObject jsonObject = JSONObject.parseObject(weatherResult);
        JSONArray jsonArray = (JSONArray) jsonObject.get("results");
        Map<String, Object> totalData = (Map<String, Object>) jsonArray.get(0);
        List<Map<String, Object>> weatherData = (List<Map<String, Object>>) totalData.get("daily");
        //今日天气
        Map<String, Object> todayWeather = weatherData.get(0);
        result = "今日天气" + "(" + todayWeather.get("date") + ")：" + todayWeather.get("text_night") + "，最高气温：" + todayWeather.get("high") + "℃，最低气温：" + todayWeather.get("low") + "℃";
        //明日天气
        Map<String, Object> tomorrowWeather = weatherData.get(0);
        result += "明日天气" + "(" + tomorrowWeather.get("date") + ")：" + tomorrowWeather.get("text_night") + "，最高气温：" + tomorrowWeather.get("high") + "℃，最低气温：" + tomorrowWeather.get("low") + "℃";
        return result;
    }
}
