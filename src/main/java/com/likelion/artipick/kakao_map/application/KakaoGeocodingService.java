package com.likelion.artipick.kakao_map.application;

import com.likelion.artipick.kakao_map.api.dto.response.AddressResponse;
import com.likelion.artipick.kakao_map.api.dto.response.CoordinatesResponse;
import com.likelion.artipick.kakao_map.api.dto.response.KakaoAddressResponse;
import com.likelion.artipick.kakao_map.api.dto.response.KakaoCoordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoGeocodingService {

    @Value("${kakao.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public AddressResponse getAddressFromCoords(double longitude, double latitude) {
        String url = String.format("https://dapi.kakao.com/v2/local/geo/coord2address.json?x=%f&y=%f", longitude, latitude);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoAddressResponse> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, KakaoAddressResponse.class
        );

        KakaoAddressResponse.Document document = response.getBody().documents().get(0);
        String regionName = document.address().region_1depth_name() + " " + document.address().region_2depth_name();

        return AddressResponse.of(regionName);
    }

    public CoordinatesResponse getCoordsFromAddress(String address) {
        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + address;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoCoordResponse> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, KakaoCoordResponse.class
        );

        KakaoCoordResponse.Document document = response.getBody().documents().get(0);
        return CoordinatesResponse.of(
                Double.parseDouble(document.x()),
                Double.parseDouble(document.y())
        );
    }
}
