package com.likelion.artipick.mypage.api;

import com.likelion.artipick.global.code.dto.ApiResponse;
import com.likelion.artipick.mypage.api.dto.MyPageDto;
import com.likelion.artipick.mypage.api.dto.ProfileUpdateDto;
import com.likelion.artipick.mypage.application.MyPageService;
import com.likelion.artipick.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/me")
@Tag(name = "마이페이지 API")
public class MyPageController {

    private final MyPageService myPageService;

    @Operation(summary = "내 마이페이지 조회")
    @GetMapping("")
    public ApiResponse<MyPageDto> getMyPage(
            @AuthenticationPrincipal User user,
            @PageableDefault(size = 10) Pageable pageable) {
        return ApiResponse.onSuccess(myPageService.getMyPage(user.getId(), pageable));
    }

    @Operation(summary = "내 정보 수정")
    @PatchMapping("")
    public ApiResponse<Void> updateProfile(
            @AuthenticationPrincipal User user,
            @RequestBody ProfileUpdateDto profileUpdateDto) {
        myPageService.updateProfile(user.getId(), profileUpdateDto);
        return ApiResponse.onSuccess(null);
    }
}

