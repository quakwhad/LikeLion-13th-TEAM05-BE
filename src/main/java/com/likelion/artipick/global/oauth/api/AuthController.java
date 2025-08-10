package com.likelion.artipick.global.oauth.api;

import com.likelion.artipick.global.code.dto.ApiResponse;
import com.likelion.artipick.global.oauth.api.dto.response.LoginResponse;
import com.likelion.artipick.global.oauth.api.dto.response.UserInfoResponse;
import com.likelion.artipick.global.oauth.application.AuthService;
import com.likelion.artipick.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "로그인 API", description = "소셜 로그인 및 토큰 재발급")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "OIDC 로그인", description = "구글/카카오에서 받은 id_token으로 로그인/회원가입 처리 후 서비스의 토큰을 발급합니다. " +
            "issuer 필드를 통해 자동으로 소셜 제공자를 판단합니다.")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestHeader("id_token") String idToken) {
        LoginResponse tokens = authService.login(idToken);
        return ApiResponse.onSuccess(tokens);
    }

      @Operation(summary = "내 정보 조회", description = "로그인된 사용자의 정보를 조회합니다. (AccessToken 필요)")
    @GetMapping("/me")
    public ApiResponse<UserInfoResponse> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ApiResponse.onFailure("AUTH401", "인증 정보가 없습니다.", null);
        }
        UserInfoResponse userInfo = authService.getUserInfo(userDetails.getUserId());
        return ApiResponse.onSuccess(userInfo);
    }

    @Operation(summary = "Access/Refresh 토큰 재발급", description = "Refresh Token을 사용하여 새로운 Access/Refresh 토큰 쌍을 발급받습니다. " +
            "DB의 Refresh Token은 새로 발급된 토큰으로 업데이트됩니다.")
    @PostMapping("/reissue")
    public ApiResponse<LoginResponse> reissueTokens(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @Parameter(description = "Refresh Token (Bearer 스키마 제외)", required = true)
            @RequestHeader("RefreshToken") String refreshToken) {

        String accessToken = authorizationHeader.substring(7);  // "Bearer " 제거
        LoginResponse newTokens = authService.reissueTokens(accessToken, refreshToken);
        return ApiResponse.onSuccess(newTokens);
    }
}
