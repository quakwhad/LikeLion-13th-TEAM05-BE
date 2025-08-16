package com.likelion.artipick.chat.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "채팅 메시지 요청")
public record ChatRequest(
        @Schema(description = "사용자 메시지", example = "오늘 갈만한 서울 전시회 추천해줘")
        @NotBlank(message = "메시지는 필수입니다.")
        @Size(max = 1000, message = "메시지는 1000자를 초과할 수 없습니다.")
        String message
) {}
