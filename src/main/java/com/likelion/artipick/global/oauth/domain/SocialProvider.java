package com.likelion.artipick.global.oauth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialProvider {
    KAKAO("kakao"),
    GOOGLE("google");

    private final String registrationId;
}
