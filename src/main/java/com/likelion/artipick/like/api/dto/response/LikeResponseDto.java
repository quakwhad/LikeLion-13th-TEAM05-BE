package com.likelion.artipick.like.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "찜하기 응답 DTO")
public record LikeResponseDto(
        @Schema(description = "찜하기 상태")
        boolean isLiked,
        @Schema(description = "응답 메시지")
        String message
) {
}