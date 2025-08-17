package com.likelion.artipick.mypage.api.dto.response;

import com.likelion.artipick.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "마이페이지에서 유저 정보 조회 응답 DTO")
public record MyPageUserResponseDto(
        @Schema(description = "유저 아이디", example = "1")
        Long id,
        @Schema(description = "닉네임", example = "닉네임")
        String nickname,
        @Schema(description = "자기소개", example = "안녕하세요")
        String introduction,
        @Schema(description = "프로필 이미지 URL", example = "http://...")
        String profileImageUrl,
        @Schema(description = "관심 카테고리 목록", example = "[\"미술\", \"음악\"]")
        List<String> interestedCategories
) {
    public static MyPageUserResponseDto from(User user, List<String> interestedCategories) {
        return new MyPageUserResponseDto(
                user.getId(),
                user.getNickname(),
                user.getIntroduction(),
                user.getProfileImageUrl(),
                interestedCategories
        );
    }
}