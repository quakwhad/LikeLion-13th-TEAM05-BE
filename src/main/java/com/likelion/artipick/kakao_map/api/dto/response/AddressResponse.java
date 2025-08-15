package com.likelion.artipick.kakao_map.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주소 변환 응답")
public record AddressResponse(
        @Schema(description = "지역명 (시/도 구/군 형태)", example = "서울특별시 구로구")
        String address
) {
    public static AddressResponse of(String address) {
        return new AddressResponse(address);
    }
}
