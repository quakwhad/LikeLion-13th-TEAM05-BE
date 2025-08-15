package com.likelion.artipick.kakao_map.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "카카오 주소 검색 API 응답")
public record KakaoAddressResponse(
        @Schema(description = "검색 결과 목록")
        List<Document> documents
) {
    @Schema(description = "주소 정보")
    public record Document(
            @Schema(description = "주소 상세 정보")
            Address address
    ) {}

    @Schema(description = "주소 상세 정보")
    public record Address(
            @Schema(description = "시/도명", example = "서울특별시")
            String region_1depth_name,
            @Schema(description = "구/군명", example = "구로구")
            String region_2depth_name
    ) {}
}
