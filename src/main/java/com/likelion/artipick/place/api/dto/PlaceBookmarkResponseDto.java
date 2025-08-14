package com.likelion.artipick.place.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "장소 북마크(관심 지역) 응답 DTO")
@Getter
@AllArgsConstructor
public class PlaceBookmarkResponseDto {
    @Schema(description = "북마크 상태")
    private final boolean isBookmarked;
    @Schema(description = "응답 메시지")
    private final String message;
}
