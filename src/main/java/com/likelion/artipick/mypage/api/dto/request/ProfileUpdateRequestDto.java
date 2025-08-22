package com.likelion.artipick.mypage.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "프로필 수정 요청 DTO")
public record ProfileUpdateRequestDto(
        @Schema(description = "닉네임", example = "새로운 닉네임")
        String nickname,
        @Schema(description = "자기소개", example = "새로운 자기소개")
        String introduction
) {
}