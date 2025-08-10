package com.likelion.artipick.like.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeResponseDto {
    @Schema(description = "찜하기 상태")
    private final boolean isLiked;
    @Schema(description = "응답 메시지")
    private final String message;
}
