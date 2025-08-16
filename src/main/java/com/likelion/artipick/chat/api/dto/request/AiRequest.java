package com.likelion.artipick.chat.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "AI 응답 요청")
public record AiRequest(
        @Schema(description = "사용자 ID", example = "123")
        @NotNull(message = "사용자 ID는 필수입니다")
        Long userId,

        @Schema(description = "AI 응답 메시지", example = "예술의 전당 OO전시회를 추천합니다.")
        @NotBlank(message = "응답 메시지는 필수입니다")
        String response
) {}
