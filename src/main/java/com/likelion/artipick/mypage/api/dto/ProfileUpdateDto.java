package com.likelion.artipick.mypage.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ProfileUpdateDto {
    @Schema(description = "닉네임", example = "새로운 닉네임")
    private String nickname;
    @Schema(description = "자기소개", example = "새로운 자기소개")
    private String introduction;
}
