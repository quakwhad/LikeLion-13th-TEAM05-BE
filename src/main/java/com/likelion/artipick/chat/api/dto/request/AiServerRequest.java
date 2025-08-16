package com.likelion.artipick.chat.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AI 서버 요청 (내부용)")
public record AiServerRequest(
        @Schema(description = "사용자 ID", example = "123")
        Long userId,

        @Schema(description = "사용자 메시지", example = "오늘 날씨 어때?")
        String message,

        @Schema(description = "타임스탬프", example = "2025-01-15T10:30:00")
        String timestamp
) {
    public static AiServerRequest of(Long userId, String message) {
        return new AiServerRequest(userId, message,
                java.time.LocalDateTime.now().toString());
    }
}
