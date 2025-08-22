package com.likelion.artipick.recommendation.api;

import com.likelion.artipick.global.code.dto.ApiResponse;
import com.likelion.artipick.global.security.CustomUserDetails;
import com.likelion.artipick.recommendation.api.dto.response.MonthlyRecommendationResponse;
import com.likelion.artipick.recommendation.api.dto.response.TodayRecommendationResponse;
import com.likelion.artipick.recommendation.application.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AI 추천", description = "AI 기반 문화행사 추천 API")
@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Operation(summary = "오늘의 추천 문화행사", description = "하루에 한 번 갱신되는 AI 기반 오늘의 추천 문화행사 2개를 조회합니다.")
    @GetMapping("/today")
    public ResponseEntity<ApiResponse<TodayRecommendationResponse>>
    getTodayRecommendations(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails != null ? userDetails.getUserId() : null;
        TodayRecommendationResponse response = recommendationService.getTodayRecommendations(userId);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "이달의 추천 문화행사", description = "한 달에 한 번 갱신되는 AI 기반 이달의 추천 문화행사 3개를 조회합니다.")
    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<MonthlyRecommendationResponse>> getMonthlyRecommendations(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails != null ? userDetails.getUserId() : null;
        MonthlyRecommendationResponse response = recommendationService.getMonthlyRecommendations(userId);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }
}
