package com.likelion.artipick.recommendation.api.dto.response;

import com.likelion.artipick.culture.api.dto.response.CultureListResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "이달의 추천 문화행사 응답")
public record MonthlyRecommendationResponse(
        @Schema(description = "추천된 문화행사 목록 (3개)")
        List<CultureListResponse> recommendations
) {
    public static MonthlyRecommendationResponse from(List<CultureListResponse> cultures) {
        return new MonthlyRecommendationResponse(cultures);
    }
}
