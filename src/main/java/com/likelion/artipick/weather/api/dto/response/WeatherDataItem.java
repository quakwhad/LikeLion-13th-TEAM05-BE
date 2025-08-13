package com.likelion.artipick.weather.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "개별 날씨 측정 데이터")
public record WeatherDataItem(
        @Schema(description = "기상 요소 코드", example = "T1H")
        String category,

        @Schema(description = "관측값 (초단기실황용)", example = "15")
        String obsrValue,

        @Schema(description = "예보값 (단기예보용)", example = "12")
        String fcstValue,

        @Schema(description = "예보 날짜", example = "20240115")
        String fcstDate,

        @Schema(description = "예보 시간", example = "1200")
        String fcstTime,

        @Schema(description = "발표 일자", example = "20240115")
        String baseDate,

        @Schema(description = "발표 시각", example = "0500")
        String baseTime,

        @Schema(description = "격자 X좌표", example = "60")
        Integer nx,

        @Schema(description = "격자 Y좌표", example = "127")
        Integer ny
) {}
