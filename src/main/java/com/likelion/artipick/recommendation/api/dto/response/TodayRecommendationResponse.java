package com.likelion.artipick.recommendation.api.dto.response;

import com.likelion.artipick.culture.api.dto.response.CultureListResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "오늘의 추천 문화행사 응답")
public record TodayRecommendationResponse(
        @Schema(description = "추천된 문화행사 목록 (2개)")
        List<CultureListResponse> recommendations
) {
    public static TodayRecommendationResponse from(List<CultureListResponse> cultures) {
        return new TodayRecommendationResponse(cultures);
    }
}
