package com.likelion.artipick.kakao_map.application;

import com.likelion.artipick.global.code.status.ErrorStatus;
import com.likelion.artipick.global.exception.GeneralException;
import com.likelion.artipick.kakao_map.api.dto.response.AddressResponse;
import com.likelion.artipick.kakao_map.api.dto.response.CoordinatesResponse;
import com.likelion.artipick.kakao_map.api.dto.response.KakaoAddressResponse;
import com.likelion.artipick.kakao_map.api.dto.response.KakaoCoordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class KakaoGeocodingService {

    @Value("${kakao.api.key}")
    private String apiKey;

    private final WebClient webClient;

    public Mono<AddressResponse> getAddressFromCoords(double longitude, double latitude) {
        String url = String.format("https://dapi.kakao.com/v2/local/geo/coord2address.json?x=%f&y=%f", longitude, latitude);
        return webClient.get()
                .uri(URI.create(url))
                .header("Authorization", "KakaoAK " + apiKey)
                .retrieve()
                .bodyToMono(KakaoAddressResponse.class)
                .map(response -> {
                    if (response.documents() == null || response.documents().isEmpty()) {
                        throw new GeneralException(ErrorStatus.LOCATION_NOT_FOUND);
                    }

                    KakaoAddressResponse.Document document = response.documents().get(0);
                    String regionName = document.address().region_1depth_name() + " " +
                            document.address().region_2depth_name();
                    return AddressResponse.of(regionName);
                })
                .onErrorMap(e -> {
                    if (e instanceof GeneralException) {
                        return e;
                    }
                    return new GeneralException(ErrorStatus.KAKAO_API_ERROR);
                });
    }

    public Mono<CoordinatesResponse> getCoordsFromAddress(String address) {
        // 한글 주소 URL 인코딩
        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + encodedAddress;

        return webClient.get()
                .uri(URI.create(url))
                .header("Authorization", "KakaoAK " + apiKey)
                .retrieve()
                .bodyToMono(KakaoCoordResponse.class)
                .map(response -> {
                    if (response.documents() == null || response.documents().isEmpty()) {
                        throw new GeneralException(ErrorStatus.LOCATION_NOT_FOUND);
                    }

                    KakaoCoordResponse.Document document = response.documents().get(0);
                    return CoordinatesResponse.of(
                            Double.parseDouble(document.x()),
                            Double.parseDouble(document.y())
                    );
                })
                .onErrorMap(e -> {
                    if (e instanceof GeneralException) {
                        return e;
                    }
                    return new GeneralException(ErrorStatus.KAKAO_API_ERROR);
                });
    }
}
