package com.likelion.artipick.weather.api;

import com.likelion.artipick.global.code.dto.ApiResponse;
import com.likelion.artipick.weather.api.dto.response.WeatherForecastInfo;
import com.likelion.artipick.weather.application.WeatherForecastService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "날씨 정보 API", description = "기상청 실시간 날씨와 단기예보를 조회합니다.")
@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherForecastController {

    private final WeatherForecastService weatherForecastService;

    @Operation(summary = "날씨 예보 조회", description = "경도, 위도로 실시간 날씨(오늘) + 예보(내일, 모레) 총 3일간 조회합니다.")
    @GetMapping("/forecast")
    public ResponseEntity<ApiResponse<WeatherForecastInfo>> getWeatherForecast(
            @Parameter(description = "경도", example = "126.555") @RequestParam double longitude,
            @Parameter(description = "위도", example = "37.555") @RequestParam double latitude) {

        WeatherForecastInfo forecast = weatherForecastService.getWeatherForecast(longitude, latitude);
        return ResponseEntity.ok(ApiResponse.onSuccess(forecast));
    }

    @Operation(summary = "현재 날씨 조회", description = "경도, 위도로 현재 실시간 날씨만 조회합니다.")
    @GetMapping("/current")
    public ResponseEntity<ApiResponse<WeatherForecastInfo.DailyForecast>> getCurrentWeather(
            @Parameter(description = "경도", example = "126.555")
            @RequestParam double longitude,
            @Parameter(description = "위도", example = "37.555")
            @RequestParam double latitude) {

        WeatherForecastInfo.DailyForecast current = weatherForecastService.getCurrentWeather(longitude, latitude);
        return ResponseEntity.ok(ApiResponse.onSuccess(current));
    }
}
