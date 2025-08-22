package com.likelion.artipick.recommendation.client.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "AI 추천 응답 DTO")
public record AiRecommendationResponse(
        @Schema(description = "추천된 문화행사 ID 목록", example = "[1, 3, 5]")
        List<Long> recommendedCultureIds
) {}
