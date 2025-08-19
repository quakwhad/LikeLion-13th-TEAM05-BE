package com.likelion.artipick.mypage.api;

import com.likelion.artipick.global.code.dto.ApiResponse;
import com.likelion.artipick.global.security.CustomUserDetails;
import com.likelion.artipick.mypage.api.dto.response.MyPagePlaceResponseDto;
import com.likelion.artipick.mypage.api.dto.response.MyPageUserResponseDto;
import com.likelion.artipick.mypage.api.dto.request.ProfileUpdateRequestDto;
import com.likelion.artipick.mypage.application.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "마이페이지 API", description = "마이페이지 조회, 수정 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/me")
public class MyPageController {

    private final MyPageService myPageService;

    @Operation(summary = "내 정보 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<MyPageUserResponseDto>> getMyPageInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        MyPageUserResponseDto myPageInfo = myPageService.getMyPageInfo(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.onSuccess(myPageInfo));
    }

    @Operation(summary = "내 정보 수정")
    @PatchMapping
    public ResponseEntity<ApiResponse<Void>> updateMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ProfileUpdateRequestDto profileUpdateRequestDto) {
        myPageService.updateProfile(userDetails.getUserId(), profileUpdateRequestDto);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @Operation(summary = "내가 북마크한 장소 목록 조회")
    @GetMapping("/location")
    public ResponseEntity<ApiResponse<Page<MyPagePlaceResponseDto>>> getMyBookmarkedPlaces(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Pageable pageable) {
        Page<MyPagePlaceResponseDto> bookmarkedPlaces = myPageService.getBookmarkedPlaces(userDetails.getUserId(), pageable);
        return ResponseEntity.ok(ApiResponse.onSuccess(bookmarkedPlaces));
    }
}

