package com.likelion.artipick.recommendation.application;

import com.likelion.artipick.culture.api.dto.response.CultureListResponse;
import com.likelion.artipick.culture.domain.Culture;
import com.likelion.artipick.culture.domain.repository.CultureRepository;
import com.likelion.artipick.global.code.status.ErrorStatus;
import com.likelion.artipick.global.exception.GeneralException;
import com.likelion.artipick.recommendation.api.dto.response.MonthlyRecommendationResponse;
import com.likelion.artipick.recommendation.api.dto.response.TodayRecommendationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RecommendationService {

    private final RedisTemplate<String, List<Long>> redisTemplate;
    private final CultureRepository cultureRepository;

    private static final String TODAY_LOGGED_KEY = "recommendation:today:logged";
    private static final String TODAY_ANONYMOUS_KEY = "recommendation:today:anonymous";
    private static final String MONTHLY_LOGGED_KEY = "recommendation:monthly:logged";
    private static final String MONTHLY_ANONYMOUS_KEY = "recommendation:monthly:anonymous";

    public TodayRecommendationResponse getTodayRecommendations(Long userId) {
        String key = userId != null ? TODAY_LOGGED_KEY : TODAY_ANONYMOUS_KEY;
        List<Long> cultureIds = redisTemplate.opsForValue().get(key);

        if (cultureIds == null || cultureIds.isEmpty()) {
            log.warn("Redis에 오늘의 추천 데이터가 없습니다. key: {}", key);
            throw new GeneralException(ErrorStatus.RECOMMENDATION_NOT_FOUND);
        }

        List<CultureListResponse> cultures = findCulturesByIds(cultureIds);
        return TodayRecommendationResponse.from(cultures);
    }

    public MonthlyRecommendationResponse getMonthlyRecommendations(Long userId) {
        String key = userId != null ? MONTHLY_LOGGED_KEY : MONTHLY_ANONYMOUS_KEY;
        List<Long> cultureIds = redisTemplate.opsForValue().get(key);

        if (cultureIds == null || cultureIds.isEmpty()) {
            log.warn("Redis에 이달의 추천 데이터가 없습니다. key: {}", key);
            throw new GeneralException(ErrorStatus.RECOMMENDATION_NOT_FOUND);
        }

        List<CultureListResponse> cultures = findCulturesByIds(cultureIds);
        return MonthlyRecommendationResponse.from(cultures);
    }

    private List<CultureListResponse> findCulturesByIds(List<Long> cultureIds) {
        List<Culture> cultures = cultureRepository.findAllById(cultureIds);

        if (cultures.size() != cultureIds.size()) {
            log.warn("일부 문화행사를 찾을 수 없습니다. 요청: {}, 조회됨: {}", cultureIds.size(), cultures.size());
        }

        return cultures.stream()
                .map(CultureListResponse::from)
                .toList();
    }
}
