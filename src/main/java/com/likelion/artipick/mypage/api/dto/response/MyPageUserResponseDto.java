package com.likelion.artipick.mypage.api.dto.response;

import com.likelion.artipick.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "마이페이지에서 유저 정보 조회 응답 DTO")
public record MyPageUserResponseDto(
        @Schema(description = "유저 아이디", example = "1")
        Long id,
        @Schema(description = "닉네임", example = "닉네임")
        String nickname,
        @Schema(description = "자기소개", example = "안녕하세요")
        String introduction,
        @Schema(description = "프로필 이미지 URL", example = "http://...")
        String profileImageUrl
) {
    public static MyPageUserResponseDto from(User user) {
        return new MyPageUserResponseDto(
                user.getId(),
                user.getNickname(),
                user.getIntroduction(),
                user.getProfileImageUrl()
        );
    }
}