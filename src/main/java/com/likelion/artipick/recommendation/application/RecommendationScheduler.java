package com.likelion.artipick.recommendation.application;

import com.likelion.artipick.culture.domain.repository.CultureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecommendationScheduler {

    private final RecommendationBatchService batchService;
    private final CultureRepository cultureRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeRecommendationsWithDelay() {
        // 7초 후에 실행 (DB 초기화 완료 대기)
        CompletableFuture.delayedExecutor(7, TimeUnit.SECONDS)
                .execute(() -> {
                    try {
                        long count = cultureRepository.count();
                        if (count > 0) {
                            // 스케줄러와 동일한 로직 호출
                            updateTodayRecommendations();
                            Thread.sleep(3000); // 3초 대기
                            updateMonthlyRecommendations();
                            log.info("지연 추천 데이터 초기화 완료");
                        }
                    } catch (Exception e) {
                        log.warn("추천 데이터 초기화 실패했지만 스케줄러가 나중에 처리합니다", e);
                    }
                });
    }

    @Scheduled(cron = "0 0 6 * * ?") // 매일 오전 6시
    public void updateTodayRecommendations() {
        log.info("오늘의 추천 스케줄 실행");
        batchService.generateTodayRecommendations();

        // 이달의 추천 유효성 검사 및 필요시 재생성
        batchService.validateAndUpdateMonthlyRecommendations();
    }

    @Scheduled(cron = "0 0 6 1 * ?") // 매월 1일 오전 6시
    public void updateMonthlyRecommendations() {
        log.info("이달의 추천 스케줄 실행");
        batchService.generateMonthlyRecommendations();
    }
}
