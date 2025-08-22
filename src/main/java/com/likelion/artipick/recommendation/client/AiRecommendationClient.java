package com.likelion.artipick.recommendation.client;

import com.likelion.artipick.global.code.status.ErrorStatus;
import com.likelion.artipick.global.exception.GeneralException;
import com.likelion.artipick.recommendation.client.dto.request.AiRecommendationRequest;
import com.likelion.artipick.recommendation.client.dto.response.AiRecommendationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class AiRecommendationClient {

    private final WebClient webClient;

    @Value("${ai.server.url}")
    private String aiBaseUrl;

    public Mono<AiRecommendationResponse> getRecommendations(AiRecommendationRequest request) {
        if (request.cultures() != null && !request.cultures().isEmpty()) {
            log.info("첫 번째 Culture: {}", request.cultures().get(0));
        }

        return webClient.post()
                .uri(aiBaseUrl + "/day_month_pik")
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AiRecommendationResponse.class)
                .doOnSuccess(response -> log.info("AI 추천 요청 성공: {} 타입, {} 개 추천받음", request.type(), response.recommendedCultureIds().size()))
                .doOnError(e -> log.error("AI 추천 요청 실패: {} 타입, 오류: {}", request.type(), e.getMessage()))
                .onErrorMap(e -> new GeneralException(ErrorStatus.AI_SERVER_ERROR));
    }
}
