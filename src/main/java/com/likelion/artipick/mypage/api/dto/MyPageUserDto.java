package com.likelion.artipick.mypage.api.dto;

import com.likelion.artipick.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MyPageUserDto {
    @Schema(description = "유저 아이디", example = "1")
    private Long id;
    @Schema(description = "닉네임", example = "닉네임")
    private String nickname;
    @Schema(description = "자기소개", example = "안녕하세요")
    private String introduction;
    @Schema(description = "프로필 이미지 URL", example = "http://...")
    private String profileImageUrl;

    public static MyPageUserDto from(User user) {
        return MyPageUserDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .introduction(user.getIntroduction())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
