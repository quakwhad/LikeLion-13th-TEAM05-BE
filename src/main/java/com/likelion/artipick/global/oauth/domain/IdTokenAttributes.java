package com.likelion.artipick.global.oauth.domain;

import com.likelion.artipick.global.oauth.domain.info.GoogleUserInfo;
import com.likelion.artipick.global.oauth.domain.info.KakaoUserInfo;
import com.likelion.artipick.global.oauth.domain.info.UserInfo;
import com.likelion.artipick.user.domain.RoleType;
import com.likelion.artipick.user.domain.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class IdTokenAttributes {

    private UserInfo userInfo;
    private SocialProvider socialProvider;

    public IdTokenAttributes(Map<String, Object> attributes, SocialProvider socialProvider){
        this.socialProvider = socialProvider;
        if(socialProvider == SocialProvider.GOOGLE) this.userInfo = new GoogleUserInfo(attributes);
        if(socialProvider == SocialProvider.KAKAO) this.userInfo = new KakaoUserInfo(attributes);
    }

    public User toUser() {
        return User.builder()
                .socialProvider(socialProvider)
                .roleType(RoleType.GUEST)
                .oauthId(userInfo.getId())
                .nickname(userInfo.getNickname())
                .profileImageUrl(userInfo.getProfileImageUrl())
                .email(userInfo.getEmail())
                .lastLoginAt(LocalDateTime.now())
                .build();
    }
}
