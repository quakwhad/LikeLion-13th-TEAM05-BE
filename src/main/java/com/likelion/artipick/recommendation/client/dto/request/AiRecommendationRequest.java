package com.likelion.artipick.recommendation.client.dto.request;

import com.likelion.artipick.recommendation.client.dto.AiCultureDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "AI 추천 요청 DTO")
public record AiRecommendationRequest(
        @Schema(description = "사용자 관심 카테고리 목록 (로그인 사용자만, 비로그인시 빈 배열)", example = "[전시, 콘서트, 뮤지컬]")
        List<String> userInterests,

        @Schema(description = "후보 문화행사 목록 (조회수, 스크랩수 포함)")
        List<AiCultureDto> cultures,

        @Schema(description = "추천 타입", example = "DAILY or MONTHLY")
        String type,

        @Schema(description = "요청할 추천 개수 (오늘: 2개, 이달: 3개)", example = "2")
        int requestCount
) {

    public static AiRecommendationRequest of(List<String> userInterests, List<AiCultureDto> cultures, String type, int requestCount) {
        return new AiRecommendationRequest(userInterests, cultures, type, requestCount);
    }
}
