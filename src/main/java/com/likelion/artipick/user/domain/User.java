package com.likelion.artipick.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

// 사용자 정보를 담는 엔티티
// 데이터베이스의 'users' 테이블과 연결
@Table(name = "users")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true) // 이메일은 중복될 수 없어요.
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Enumerated(EnumType.STRING) // RoleType이라는 Enum 값을 DB에 문자열로 저장해요.
    @Column(name = "role_type", nullable = false)
    private RoleType roleType;

    @Column(name = "oauth_id")
    private String oauthId;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Builder
    public User(String email, String nickname, String introduction, String profileImageUrl, RoleType roleType, String oauthId, LocalDateTime lastLoginAt) {
        this.email = email;
        this.nickname = nickname;
        this.introduction = introduction;
        this.profileImageUrl = profileImageUrl;
        this.roleType = roleType;
        this.oauthId = oauthId;
        this.lastLoginAt = lastLoginAt;
    }

    // Refresh Token 업데이트
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // 닉네임 업데이트
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    // 자기소개 업데이트
    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }

    // 닉네임과 자기소개를 한 번에 업데이트
    public void updateProfile(String nickname, String introduction) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (introduction != null) {
            this.introduction = introduction;
        }
    }

    // 프로필 이미지 업데이트
    public void updateProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    // 마지막 로그인 시간 기록
    public void markLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }

    // 사용자 권한을 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(roleType.name()));
    }

    // Spring Security를 위한 기본 메서드
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}