package com.likelion.artipick.place.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장소 북마크(관심 지역) 응답 DTO")
public record PlaceBookmarkResponseDto(
        @Schema(description = "북마크 상태")
        boolean isBookmarked,
        @Schema(description = "응답 메시지")
        String message
) {
}