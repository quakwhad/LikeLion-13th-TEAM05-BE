package com.likelion.artipick.weather.util;

import org.springframework.stereotype.Component;

@Component
public class WeatherCodeConverter {

    public String convertSkyCode(String skyCode) {
        return switch (skyCode) {
            case "1" -> "맑음";
            case "3" -> "구름많음";
            case "4" -> "흐림";
            default -> "정보없음";
        };
    }

    public String convertPrecipitationType(String ptyCode) {
        return switch (ptyCode) {
            case "0" -> "없음";
            case "1" -> "비";
            case "2" -> "비/눈";
            case "3" -> "눈";
            case "5" -> "빗방울";
            case "6" -> "빗방울눈날림";
            case "7" -> "눈날림";
            default -> "정보없음";
        };
    }
}
