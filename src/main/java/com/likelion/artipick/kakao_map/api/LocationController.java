package com.likelion.artipick.kakao_map.api;

import com.likelion.artipick.global.code.dto.ApiResponse;
import com.likelion.artipick.kakao_map.api.dto.response.AddressResponse;
import com.likelion.artipick.kakao_map.api.dto.response.CoordinatesResponse;
import com.likelion.artipick.kakao_map.application.KakaoGeocodingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "위치 API", description = "좌표와 주소 변환")
@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {

    private final KakaoGeocodingService geocodingService;

    @Operation(summary = "좌표를 주소로 변환", description = "경도, 위도를 지역명(시/구/군)으로 변환합니다.")
    @GetMapping("/address")
    public ResponseEntity<ApiResponse<AddressResponse>> getAddress(
            @Parameter(description = "경도", example = "126.978") @RequestParam double longitude,
            @Parameter(description = "위도", example = "37.566") @RequestParam double latitude) {
        AddressResponse address = geocodingService.getAddressFromCoords(longitude, latitude).block();
        return ResponseEntity.ok(ApiResponse.onSuccess(address));
    }

    @Operation(summary = "주소를 좌표로 변환", description = "지역명을 경도, 위도로 변환합니다.")
    @GetMapping("/coordinates")
    public ApiResponse<CoordinatesResponse> getCoordinates(
            @Parameter(description = "주소", example = "서울특별시 구로구") @RequestParam String address) {
        CoordinatesResponse coordinates = geocodingService.getCoordsFromAddress(address).block();
        return ApiResponse.onSuccess(coordinates);
    }
}
