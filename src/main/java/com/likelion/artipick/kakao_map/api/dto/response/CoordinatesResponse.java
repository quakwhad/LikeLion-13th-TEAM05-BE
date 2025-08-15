package com.likelion.artipick.kakao_map.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "좌표 변환 응답")
public record CoordinatesResponse(
        @Schema(description = "경도", example = "126.978")
        double longitude,
        @Schema(description = "위도", example = "37.566")
        double latitude
) {
    public static CoordinatesResponse of(double longitude, double latitude) {
        return new CoordinatesResponse(longitude, latitude);
    }
}
