package com.likelion.artipick.weather.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.artipick.global.code.status.ErrorStatus;
import com.likelion.artipick.global.exception.GeneralException;
import com.likelion.artipick.weather.api.dto.response.WeatherApiResponse;
import com.likelion.artipick.weather.api.dto.response.WeatherDataItem;
import com.likelion.artipick.weather.api.dto.response.WeatherForecastInfo;
import com.likelion.artipick.weather.client.WeatherApiClient;
import com.likelion.artipick.weather.util.CoordinateConverter;
import com.likelion.artipick.weather.util.WeatherCodeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class WeatherForecastService {

    private final CoordinateConverter coordinateConverter;
    private final WeatherApiClient weatherApiClient;
    private final WeatherCodeConverter weatherCodeConverter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WeatherForecastInfo getWeatherForecast(double longitude, double latitude) {
        validateCoordinates(longitude, latitude);
        CoordinateConverter.GridCoordinate grid = coordinateConverter.convertToGrid(longitude,
                latitude);

        try {
            String currentData = weatherApiClient.getCurrentWeather(grid.x(), grid.y());
            String forecastData = weatherApiClient.getForecastWeather(grid.x(), grid.y());

            String region = String.format("경도:%.3f, 위도:%.3f", longitude, latitude);
            List<WeatherForecastInfo.DailyForecast> forecasts = parseForecastData(currentData, forecastData, region);

            return new WeatherForecastInfo(forecasts);

        } catch (Exception e) {
            log.error("날씨 예보 조회 중 오류 발생", e);
            throw e;
        }
    }

    public WeatherForecastInfo.DailyForecast getCurrentWeather(double longitude, double latitude) {
        validateCoordinates(longitude, latitude);

        CoordinateConverter.GridCoordinate grid = coordinateConverter.convertToGrid(longitude, latitude);
        String currentData = weatherApiClient.getCurrentWeather(grid.x(), grid.y());
        String region = String.format("경도:%.3f, 위도:%.3f", longitude, latitude);

        return parseCurrentWeather(currentData, LocalDate.now(), region);
    }

    private void validateCoordinates(double longitude, double latitude) {
        // 한국 영토 범위로 제한
        if (latitude < 33.0 || latitude > 43.0) {
            throw new GeneralException(ErrorStatus.INVALID_LATITUDE);
        }
        if (longitude < 124.0 || longitude > 132.0) {
            throw new GeneralException(ErrorStatus.INVALID_LONGITUDE);
        }
    }

    private List<WeatherForecastInfo.DailyForecast> parseForecastData(String currentData, String forecastData, String region) {
        List<WeatherForecastInfo.DailyForecast> forecasts = new ArrayList<>();

        // 실시간 데이터
        forecasts.add(parseCurrentWeather(currentData, LocalDate.now(), region));

        // 내일, 모레 (예보)
        for (int i = 1; i < 3; i++) {
            forecasts.add(parseForecastWeather(forecastData, LocalDate.now().plusDays(i), region));
        }
        return forecasts;
    }

    private WeatherForecastInfo.DailyForecast parseCurrentWeather(String jsonData, LocalDate date, String region) {
        try {
            WeatherApiResponse response = objectMapper.readValue(jsonData, WeatherApiResponse.class);
            validateApiResponse(response);

            if (response.body() == null || response.body().items() == null) {
                log.warn("날씨 API 응답에 body 또는 items가 없습니다. JSON: {}", jsonData);
                return createEmptyForecast(date, region);
            }

            List<WeatherDataItem> items = response.body().items().item();

            Map<String, String> weatherDataMap = items.stream()
                    .filter(item -> item.obsrValue() != null)
                    .collect(Collectors.toMap(
                            WeatherDataItem::category,
                            WeatherDataItem::obsrValue,
                            (existing, replacement) -> existing // 중복 키 처리
                    ));

            return buildDailyForecast(weatherDataMap, date, true, region);
        } catch (Exception e) {
            log.error("현재 날씨 데이터 파싱 중 오류 발생. JSON: {}", jsonData, e);
            throw new GeneralException(ErrorStatus.WEATHER_API_ERROR);
        }
    }

    private WeatherForecastInfo.DailyForecast parseForecastWeather(String jsonData, LocalDate date, String region) {
        try {
            WeatherApiResponse response = objectMapper.readValue(jsonData, WeatherApiResponse.class);
            validateApiResponse(response);

            List<WeatherDataItem> items = response.body().items().item();
            String targetDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            // 해당 날짜의 데이터만 먼저 필터링
            List<WeatherDataItem> targetDateItems = items.stream()
                    .filter(item -> targetDate.equals(item.fcstDate()) && item.fcstValue() != null)
                    .toList();

            if (targetDateItems.isEmpty()) {
                return createEmptyForecast(date, region);
            }

            // 가장 최근 시간 찾기
            String latestTime = targetDateItems.stream()
                    .map(WeatherDataItem::fcstTime)
                    .max(String::compareTo)
                    .orElse(null);

            // 최근 시간 데이터로 Map 생성
            Map<String, String> weatherDataMap = targetDateItems.stream()
                    .filter(item -> latestTime.equals(item.fcstTime()))
                    .collect(Collectors.toMap(
                            WeatherDataItem::category,
                            WeatherDataItem::fcstValue,
                            (existing, replacement) -> existing
                    ));

            return buildDailyForecast(weatherDataMap, date, false, region);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.WEATHER_FORECAST_ERROR);
        }
    }

    private void validateApiResponse(WeatherApiResponse response) {
        if (response == null || response.header() == null) {
            throw new GeneralException(ErrorStatus.WEATHER_API_ERROR);
        }

        String resultCode = response.header().resultCode();
        if (!"00".equals(resultCode)) {
            throw new GeneralException(ErrorStatus.WEATHER_API_ERROR);
        }
    }

    private WeatherForecastInfo.DailyForecast buildDailyForecast(Map<String, String> weatherData, LocalDate date, boolean isCurrent, String region) {
        String temperature = weatherData.getOrDefault(isCurrent ? "T1H" : "TMP", "정보없음");
        String skyCondition = weatherData.getOrDefault("SKY", "1");
        String precipitationProbability = isCurrent ? "정보없음" : weatherData.getOrDefault("POP", "0") + "%";
        String precipitationType = weatherData.getOrDefault("PTY", "0");
        String precipitationAmount = formatPrecipitationAmount(weatherData.getOrDefault(isCurrent ? "RN1" : "PCP", "0"));

        return new WeatherForecastInfo.DailyForecast(
                region,
                date.toString(),
                temperature.contains("도") ? temperature : temperature + "도",
                skyCondition,
                precipitationProbability,
                precipitationType,
                precipitationAmount
        );
    }

    private String formatPrecipitationAmount(String amount) {
        if (amount == null || "0".equals(amount) || "강수없음".equals(amount)) {
            return "0mm";
        }
        return amount.contains("mm") ? amount : amount + "mm";
    }

    private WeatherForecastInfo.DailyForecast createEmptyForecast(LocalDate date, String region) {
        return new WeatherForecastInfo.DailyForecast(
                region,
                date.toString(),
                "데이터없음",
                "예보없음",
                "0%",
                "없음",
                "0mm"
        );
    }
}
