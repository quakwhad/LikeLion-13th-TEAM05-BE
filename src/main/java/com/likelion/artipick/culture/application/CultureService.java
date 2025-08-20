package com.likelion.artipick.culture.application;

import com.likelion.artipick.culture.api.dto.request.CultureRequest;
import com.likelion.artipick.culture.api.dto.response.CultureDetailApiResponse;
import com.likelion.artipick.culture.client.CultureApiClient;
import com.likelion.artipick.culture.domain.Category;
import com.likelion.artipick.culture.domain.Culture;
import com.likelion.artipick.culture.domain.CultureLike;
import com.likelion.artipick.culture.domain.repository.CultureLikeRepository;
import com.likelion.artipick.culture.domain.repository.CultureRepository;
import com.likelion.artipick.global.code.status.ErrorStatus;
import com.likelion.artipick.global.exception.GeneralException;
import com.likelion.artipick.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CultureService {

    private final CultureRepository cultureRepository;
    private final CultureApiClient cultureApiClient;
    private final CultureLikeRepository likeRepository;

    @Transactional
    public Mono<Void> scheduledSync() {
        return Flux.range(1, 3)
                .concatMap(page -> syncCultureEventsByPageReactive(page, 35))// concatMap (순서 보장)
                .then()
                .doOnSuccess(unused -> log.info("전체 동기화 완료"))
                .doOnError(error -> log.error("전체 동기화 실패", error));
    }

    public Page<Culture> findAllCultures(Pageable pageable) {
        return cultureRepository.findAll(pageable);
    }

    @Transactional
    public Culture findCultureById(Long id) {
        Culture culture = cultureRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_FOUND));
        culture.increaseViewCount(); //조회수 증가
        return culture;
    }

    private Mono<List<Culture>> syncCultureEventsByPageReactive(int page, int size) {
        log.info("페이지 {} 동기화 시작 (요청 크기: {})", page, size);

        return cultureApiClient.getCultureSeqList(page, size)
                .onErrorResume(throwable -> {
                    log.error("페이지 {} seq 목록 조회 실패", page, throwable);
                    return Mono.just(List.of());
                })
                .flatMapMany(Flux::fromIterable)
                .flatMap(seq ->
                                Mono.fromCallable(() -> cultureRepository.existsBySeq(seq))
                                        .subscribeOn(Schedulers.boundedElastic())
                                        .map(exists -> exists ? null : seq)
                                        .onErrorResume(throwable -> {
                                            log.debug("seq {} 존재 여부 확인 실패: {}", seq, throwable.getMessage());
                                            return Mono.empty();
                                        })
                        , 3)
                .filter(Objects::nonNull)
                .flatMap(seq -> saveDetailData(seq)
                                .onErrorResume(throwable -> {
                                    log.warn("seq {} 저장 실패, 스킵: {}", seq, throwable.getMessage());
                                    return Mono.empty();
                                })
                        , 2)
                .collectList()
                .doOnNext(cultures ->
                        log.info("페이지 {} 동기화 완료: {} 건 성공", page, cultures.size())
                );
    }

    private Mono<Culture> saveDetailData(String seq) {
        return cultureApiClient.getCultureEventBySeq(seq)
                .timeout(Duration.ofSeconds(15))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))
                .map(this::extractCultureItem)
                .map(this::convertToEntity)
                .flatMap(culture ->
                        Mono.fromCallable(() -> {
                                    try {
                                        return cultureRepository.save(culture);
                                    } catch (DataIntegrityViolationException e) {
                                        log.debug("중복 seq 스킵: {}", seq);
                                        return culture;
                                    } catch (Exception e) {
                                        log.error("seq {} 저장 중 예상치 못한 오류", seq, e);
                                        throw new GeneralException(ErrorStatus.DATABASE_ERROR);
                                    }
                                })
                                .subscribeOn(Schedulers.boundedElastic())
                );
    }

    private CultureDetailApiResponse.CultureDetailItem extractCultureItem(CultureDetailApiResponse response) {
        if (response == null || response.getBody() == null || response.getBody().getItems() == null ||
                response.getBody().getItems().getItem() == null || response.getBody().getItems().getItem().isEmpty()) {
            throw new GeneralException(ErrorStatus.CULTURE_API_EMPTY_RESPONSE);
        }
        return response.getBody().getItems().getItem().get(0);
    }

    private Culture convertToEntity(CultureDetailApiResponse.CultureDetailItem item) {
        return Culture.builder()
                .seq(item.getSeq())
                .title(item.getTitle())
                .startDate(parseDate(item.getStartDate()))
                .endDate(parseDate(item.getEndDate()))
                .place(item.getPlace())
                .category(convertToCategory(item.getRealmName()))
                .area(item.getArea())
                .sigungu(item.getSigungu())
                .price(item.getPrice())
                .placeAddr(item.getPlaceAddr())
                .gpsX(item.getGpsX())
                .gpsY(item.getGpsY())
                .placeUrl(item.getPlaceUrl())
                .phone(item.getPhone())
                .contents(item.getContents())
                .imgUrl(item.getImgUrl())
                .isFromApi(true)
                .build();
    }

    private LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyyMMdd"));
        } catch (Exception e) {
            return LocalDate.now();
        }
    }

    private Category convertToCategory(String realmName) {
        return Arrays.stream(Category.values())
                .filter(category -> category.getDisplayName().equals(realmName))
                .findFirst()
                .orElse(Category.ETC);
    }

    private void validateOwnership(Culture culture, User user) {
        if (!culture.getUser().equals(user)) {
            throw new GeneralException(ErrorStatus.FORBIDDEN);
        }
    }

    // CRUD/찜 기능

    @Transactional
    public Culture createCulture(CultureRequest request, User user) {
        Culture culture = Culture.builder()
                .title(request.title())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .place(request.place())
                .category(request.category())
                .area(request.area())
                .sigungu(request.sigungu())
                .price(request.price())
                .placeAddr(request.placeAddr())
                .gpsX(request.gpsX())
                .gpsY(request.gpsY())
                .placeUrl(request.placeUrl())
                .phone(request.phone())
                .contents(request.contents())
                .imgUrl(request.imgUrl())
                .isFromApi(false)
                .user(user)
                .build();
        return cultureRepository.save(culture);
    }

    @Transactional
    public Culture updateCulture(Long id, CultureRequest request, User user) {
        Culture culture = findCultureById(id);
        validateOwnership(culture, user);
        culture.updateCulture(
                request.title(),
                request.startDate(),
                request.endDate(),
                request.place(),
                request.category(),
                request.area(),
                request.sigungu(),
                request.price(),
                request.placeAddr(),
                request.gpsX(),
                request.gpsY(),
                request.placeUrl(),
                request.phone(),
                request.contents(),
                request.imgUrl()
        );
        return culture;
    }

    @Transactional
    public void deleteCulture(Long id, User user) {
        Culture culture = findCultureById(id);
        validateOwnership(culture, user);
        cultureRepository.delete(culture);
    }

    @Transactional
    public void toggleLike(Long cultureId, User user) {
        Culture culture = findCultureById(cultureId);
        CultureLike like = likeRepository.findByUserAndCulture(user, culture)
                .orElse(new CultureLike(user, culture));
        like.toggle();
        likeRepository.save(like);
    }

    public Map<String, String> getLocation(Long cultureId) {
        Culture culture = findCultureById(cultureId);
        return Map.of(
                "gpsX", culture.getGpsX(),
                "gpsY", culture.getGpsY()
        );
    }

}
