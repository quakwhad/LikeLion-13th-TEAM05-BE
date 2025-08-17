package com.likelion.artipick.culture.client;

import com.likelion.artipick.culture.api.dto.response.CultureDetailApiResponse;
import com.likelion.artipick.culture.api.dto.response.CultureListApiResponse;
import com.likelion.artipick.global.code.status.ErrorStatus;
import com.likelion.artipick.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CultureApiClient {

    private final WebClient webClient;

    @Value("${culture.api.base-url}")
    private String baseUrl;

    @Value("${culture.api.auth-key}")
    private String authKey;

    public Mono<CultureListApiResponse> getCultureEvents(int pageNo, int numOfRows) {
        String encodedKey = URLEncoder.encode(authKey, StandardCharsets.UTF_8);
        String url = String.format("%s/period2?serviceKey=%s&PageNo=%d&numOfrows=%d", baseUrl, encodedKey, pageNo, numOfRows);

        return webClient.get()
                .uri(URI.create(url))
                .accept(MediaType.APPLICATION_XML)
                .retrieve()
                .bodyToMono(CultureListApiResponse.class)
                .onErrorMap(e -> {
                    log.error("Culture API 호출 실패", e);
                    return new GeneralException(ErrorStatus.CULTURE_API_ERROR);
                });
    }

    public Mono<CultureDetailApiResponse> getCultureEventBySeq(String seq) {
        String encodedKey = URLEncoder.encode(authKey, StandardCharsets.UTF_8);
        String url = String.format("%s/detail2?serviceKey=%s&seq=%s", baseUrl, encodedKey, seq);

        return webClient.get()
                .uri(URI.create(url))
                .accept(MediaType.APPLICATION_XML)
                .retrieve()
                .bodyToMono(CultureDetailApiResponse.class)
                .onErrorMap(e -> {
                    log.error("Culture detail API 호출 실패: seq={}", seq, e);
                    return new GeneralException(ErrorStatus.CULTURE_API_ERROR);
                });
    }

    public Mono<List<String>> getCultureSeqList(int pageNo, int numOfRows) {
        return getCultureEvents(pageNo, numOfRows)
                .map(this::extractSeqList);
    }

    private List<String> extractSeqList(CultureListApiResponse response) {
        if (response.getBody() == null || response.getBody().getItems() == null || response.getBody().getItems().getItem() == null) {
            return List.of();
        }

        return response.getBody().getItems().getItem()
                .stream()
                .map(CultureListApiResponse.CultureListItem::getSeq)
                .toList();
    }
}
