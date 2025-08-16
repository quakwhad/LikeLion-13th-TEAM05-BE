package com.likelion.artipick.weather.client;

import com.likelion.artipick.global.code.status.ErrorStatus;
import com.likelion.artipick.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class WeatherApiClient {

    private final RestTemplate restTemplate;

    @Value("${weather.api.base-url}")
    private String baseUrl;

    @Value("${weather.api.auth-key}")
    private String key;

    @Value("${weather.api.current-endpoint}")
    private String currentEndpoint;

    @Value("${weather.api.forecast-endpoint}")
    private String forecastEndpoint;

    public String getCurrentWeather(int nx, int ny) {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour() - 1;
        LocalDate date = now.toLocalDate();

        if (hour < 0) {
            hour = 23;
            date = date.minusDays(1);
        }

        String baseDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = String.format("%02d00", hour);

        String encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8);
        String url = String.format("%s%s?ServiceKey=%s&pageNo=1&numOfRows=1000&dataType=JSON&base_date=%s&base_time=%s&nx=%d&ny=%d",
                baseUrl, currentEndpoint, encodedKey, baseDate, baseTime, nx, ny);

        URI uri = URI.create(url);

        try {
            return restTemplate.getForObject(uri, String.class);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.WEATHER_API_ERROR);
        }
    }

    public String getForecastWeather(int nx, int ny) {
        LocalDateTime now = LocalDateTime.now();
        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = getForecastBaseTime(now);

        String encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8);
        String url = String.format("%s%s?ServiceKey=%s&pageNo=1&numOfRows=1000&dataType=JSON&base_date=%s&base_time=%s&nx=%d&ny=%d",
                baseUrl, forecastEndpoint, encodedKey, baseDate, baseTime, nx, ny);

        URI uri = URI.create(url);

        try {
            return restTemplate.getForObject(uri, String.class);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.WEATHER_API_ERROR);
        }
    }

    private String getForecastBaseTime(LocalDateTime now) {
        int hour = now.getHour();

        // 단기예보 발표시간: 02, 05, 08, 11, 14, 17, 20, 23시
        int[] baseTimes = {2, 5, 8, 11, 14, 17, 20, 23};

        for (int i = baseTimes.length - 1; i >= 0; i--) {
            if (hour >= baseTimes[i]) {
                return String.format("%02d00", baseTimes[i]);
            }
        }

        return "2300"; // 기본값 (23시)
    }
}
