package com.likelion.artipick.weather.api.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collections;
import java.util.List;

@Schema(description = "기상청 API 응답")
public record WeatherApiResponse(
        @Schema(description = "응답 헤더")
        WeatherApiHeader header,

        @Schema(description = "응답 바디")
        WeatherApiBody body
) {

    @JsonCreator
    public WeatherApiResponse(@JsonProperty("response") ResponseContainer response) {
        this(response.header(), response.body());
    }

    public record ResponseContainer(
            WeatherApiHeader header,
            WeatherApiBody body
    ) {}

    @Schema(description = "기상청 API 응답 헤더")
    public record WeatherApiHeader(
            @Schema(description = "결과 코드", example = "00")
            String resultCode,

            @Schema(description = "결과 메시지", example = "NORMAL_SERVICE")
            String resultMsg
    ) {}

    @Schema(description = "기상청 API 응답 본문")
    public record WeatherApiBody(
            @Schema(description = "데이터 타입", example = "JSON")
            String dataType,

            @Schema(description = "날씨 데이터 목록")
            Items items,

            @Schema(description = "페이지 번호", example = "1")
            Integer pageNo,

            @Schema(description = "한 페이지 결과 수", example = "1000")
            Integer numOfRows,

            @Schema(description = "전체 데이터 개수", example = "60")
            Integer totalCount
    ) {
        @Schema(description = "날씨 데이터 아이템 목록")
        public record Items(
                @Schema(description = "개별 날씨 데이터")
                List<WeatherDataItem> item
        ) {
            @JsonCreator
            public Items(List<WeatherDataItem> item) {
                this.item = (item == null) ? Collections.emptyList() : item;
            }
        }

    }
}
