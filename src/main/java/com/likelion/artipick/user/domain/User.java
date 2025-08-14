package com.likelion.artipick.user.domain;

import com.likelion.artipick.global.entity.BaseEntity;
import com.likelion.artipick.global.oauth.domain.SocialProvider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "users")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false)
    private RoleType roleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_provider")
    private SocialProvider socialProvider;

    @Column(name = "oauth_id")
    private String oauthId;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Builder
    public User(String email, String nickname, String introduction, String profileImageUrl, RoleType roleType, SocialProvider socialProvider, String oauthId, LocalDateTime lastLoginAt) {
        this.email = email;
        this.nickname = nickname;
        this.introduction = introduction;
        this.profileImageUrl = profileImageUrl;
        this.roleType = roleType;
        this.socialProvider = socialProvider;
        this.oauthId = oauthId;
        this.lastLoginAt = lastLoginAt;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void updateProfile(String nickname, String introduction) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (introduction != null) {
            this.introduction = introduction;
        }
    }

    public void updateProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void markLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }
}