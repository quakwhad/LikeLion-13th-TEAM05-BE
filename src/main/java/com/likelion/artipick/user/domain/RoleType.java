package com.likelion.artipick.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 사용자 역할을 정의하는 Enum
// 관리자, 일반 사용자, 게스트로 구분
@Getter
@RequiredArgsConstructor
public enum RoleType {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    GUEST("ROLE_GUEST");

    private final String key;
}