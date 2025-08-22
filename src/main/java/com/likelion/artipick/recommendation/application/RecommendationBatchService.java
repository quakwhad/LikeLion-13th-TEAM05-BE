package com.likelion.artipick.recommendation.application;

import com.likelion.artipick.culture.domain.Culture;
import com.likelion.artipick.culture.domain.repository.CultureRepository;
import com.likelion.artipick.interestcategory.domain.InterestCategory;
import com.likelion.artipick.interestcategory.domain.repository.InterestCategoryRepository;
import com.likelion.artipick.recommendation.client.AiRecommendationClient;
import com.likelion.artipick.recommendation.client.dto.AiCultureDto;
import com.likelion.artipick.recommendation.client.dto.request.AiRecommendationRequest;
import com.likelion.artipick.recommendation.client.dto.response.AiRecommendationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RecommendationBatchService {

    private final AiRecommendationClient aiClient;
    private final RedisTemplate<String, List<Long>> redisTemplate;
    private final CultureRepository cultureRepository;
    private final InterestCategoryRepository interestCategoryRepository;

    private static final String TODAY_LOGGED_KEY = "recommendation:today:logged";
    private static final String TODAY_ANONYMOUS_KEY = "recommendation:today:anonymous";
    private static final String MONTHLY_LOGGED_KEY = "recommendation:monthly:logged";
    private static final String MONTHLY_ANONYMOUS_KEY = "recommendation:monthly:anonymous";

    public void generateTodayRecommendations() {
        List<Culture> availableCultures = findAvailableCultures();
        List<AiCultureDto> cultureDtos = availableCultures.stream()
                .map(AiCultureDto::from)
                .toList();

        // 로그인 사용자용 (관심 카테고리 포함)
        List<String> userInterests = findPopularCategories();
        generateAndCacheRecommendation(cultureDtos, userInterests, "TODAY", 2, TODAY_LOGGED_KEY, 24);

        try {
            Thread.sleep(3000); // 3초 대기
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 비로그인 사용자용 (관심 카테고리 없음)
        generateAndCacheRecommendation(cultureDtos, null, "TODAY", 2, TODAY_ANONYMOUS_KEY, 24);
    }

    public void generateMonthlyRecommendations() {
        List<Culture> availableCultures = findAvailableCultures();
        List<AiCultureDto> cultureDtos = availableCultures.stream()
                .map(AiCultureDto::from)
                .toList();

        // 로그인 사용자용
        List<String> userInterests = findPopularCategories();
        generateAndCacheRecommendation(cultureDtos, userInterests, "MONTHLY", 3, MONTHLY_LOGGED_KEY, 24 * 30);

        // 비로그인 사용자용
        generateAndCacheRecommendation(cultureDtos, null, "MONTHLY", 3, MONTHLY_ANONYMOUS_KEY, 24 * 30);

    }

    public void validateAndUpdateMonthlyRecommendations() {
        LocalDate today = LocalDate.now();

        validateAndUpdateIfNeeded(MONTHLY_LOGGED_KEY, today, true);
        validateAndUpdateIfNeeded(MONTHLY_ANONYMOUS_KEY, today, false);
    }

    private void validateAndUpdateIfNeeded(String cacheKey, LocalDate today, boolean isLoggedUser) {
        List<Long> cachedIds = redisTemplate.opsForValue().get(cacheKey);

        if (cachedIds != null && !cachedIds.isEmpty()) {
            // 유효한 문화행사 개수 확인
            List<Culture> validCultures = cultureRepository.findByIdInAndEndDateGreaterThanEqual(cachedIds, today);

            // 하나라도 기간이 지났으면 전체 재생성
            if (validCultures.size() < cachedIds.size()) {
                log.info("이달의 추천 재생성: {} - 원본: {}, 유효: {}", cacheKey, cachedIds.size(), validCultures.size());
                regenerateMonthlyRecommendation(isLoggedUser);
            }
        }
    }

    private void regenerateMonthlyRecommendation(boolean isLoggedUser) {
        List<Culture> availableCultures = findAvailableCultures();
        List<AiCultureDto> cultureDtos = availableCultures.stream()
                .map(AiCultureDto::from)
                .toList();

        if (isLoggedUser) {
            List<String> userInterests = findPopularCategories();
            generateAndCacheRecommendation(cultureDtos, userInterests, "MONTHLY", 3, MONTHLY_LOGGED_KEY, 24 * 30);
        } else {
            generateAndCacheRecommendation(cultureDtos, null, "MONTHLY", 3, MONTHLY_ANONYMOUS_KEY, 24 * 30);
        }
    }

    private void generateAndCacheRecommendation(List<AiCultureDto> cultures, List<String> userInterests,
                                                String type, int count, String cacheKey, int ttlHours) {
        try {
            AiRecommendationRequest request = AiRecommendationRequest.of(userInterests, cultures, type, count);
            AiRecommendationResponse response = aiClient.getRecommendations(request).block();

            if (response != null && response.recommendedCultureIds() != null) {
                redisTemplate.opsForValue().set(cacheKey, response.recommendedCultureIds(), ttlHours, TimeUnit.HOURS);
            }
        } catch (Exception e) {
            log.error("추천 생성 실패: {}", cacheKey, e);
        }
    }

    private List<Culture> findAvailableCultures() {
        LocalDate today = LocalDate.now();
        List<Culture> allCultures = cultureRepository.findByEndDateGreaterThanEqualOrderByViewCountDescLikeCountDesc(today);
        
        // AI 서버 부하 방지를 위해 인기 상위 50개만 전송
        return allCultures.stream()
                .limit(50)
                .toList();
    }

    private List<String> findPopularCategories() {
        List<InterestCategory> interests = interestCategoryRepository.findAll();
        return interests.stream()
                .map(interest -> interest.getCategory().name())
                .distinct()
                .toList();
    }
}
