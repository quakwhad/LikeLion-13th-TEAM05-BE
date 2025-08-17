package com.likelion.artipick.interestcategory.api;

import com.likelion.artipick.global.code.dto.ApiResponse;
import com.likelion.artipick.global.security.CustomUserDetails;
import com.likelion.artipick.interestcategory.api.dto.request.InterestCategoryRequestDto;
import com.likelion.artipick.interestcategory.application.InterestCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관심 카테고리 API", description = "관심 카테고리 설정 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interest-categories")
public class InterestCategoryController {

    private final InterestCategoryService interestCategoryService;

    @Operation(summary = "관심 카테고리 설정/업데이트")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> updateInterestCategories(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody InterestCategoryRequestDto requestDto) {
        interestCategoryService.updateInterestCategories(userDetails.getUserId(), requestDto);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }
}
