package com.likelion.artipick.kakao_map.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "카카오 좌표 검색 API 응답")
public record KakaoCoordResponse(
        @Schema(description = "검색 결과 목록")
        List<Document> documents
) {
    @Schema(description = "좌표 정보")
    public record Document(
            @Schema(description = "경도", example = "126.978")
            String x,
            @Schema(description = "위도", example = "37.566")
            String y
    ) {}
}
