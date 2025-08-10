package com.likelion.artipick.global.oauth.api.dto.response;

import com.likelion.artipick.user.domain.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponse {
    private String email;
    private String name;
    private String profileImageUrl;
    private RoleType roleType;
}
