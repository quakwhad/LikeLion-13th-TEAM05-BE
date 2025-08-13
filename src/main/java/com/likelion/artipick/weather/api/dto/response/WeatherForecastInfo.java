package com.likelion.artipick.weather.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "날씨 예보 정보")
public record WeatherForecastInfo(
        @Schema(description = "일별 예보 목록")
        List<DailyForecast> forecasts
) {
    @Schema(description = "일별 날씨 예보")
    public record DailyForecast(
            @Schema(description = "지역명", example = "경도:126.978, 위도:37.566")
            String region,

            @Schema(description = "날짜", example = "2024-01-15")
            String date,

            @Schema(description = "기온", example = "5도")
            String temperature,

            @Schema(description = "하늘상태", example = "맑음")
            String skyCondition,

            @Schema(description = "강수확률", example = "10%")
            String precipitationProbability,

            @Schema(description = "강수형태", example = "없음")
            String precipitationType,

            @Schema(description = "강수량", example = "0mm")
            String precipitationAmount
    ) {}
}
