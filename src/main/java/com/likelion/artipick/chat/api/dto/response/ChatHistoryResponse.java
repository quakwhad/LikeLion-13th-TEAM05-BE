package com.likelion.artipick.chat.api.dto.response;

import com.likelion.artipick.chat.domain.MessageType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "채팅 기록 응답")
public record ChatHistoryResponse(
        @Schema(description = "메시지 내용", example = "전시회 추천")
        String content,

        @Schema(description = "메시지 타입", example = "USER")
        MessageType messageType,

        @Schema(description = "전송 시간", example = "2025-01-15T10:30:00")
        LocalDateTime timestamp
) {
    public static ChatHistoryResponse of(String content, MessageType messageType, LocalDateTime timestamp) {
        return new ChatHistoryResponse(content, messageType, timestamp);
    }
}
