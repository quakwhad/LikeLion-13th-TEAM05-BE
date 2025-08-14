package com.likelion.artipick.place.api;

import com.likelion.artipick.global.code.dto.ApiResponse;
import com.likelion.artipick.global.security.CustomUserDetails;
import com.likelion.artipick.place.api.dto.PlaceBookmarkResponseDto;
import com.likelion.artipick.place.application.PlaceBookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "장소 북마크 API", description = "관심 장소 북마크/취소")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/places")
public class PlaceBookmarkController {

    private final PlaceBookmarkService placeBookmarkService;

    @Operation(summary = "장소 북마크/취소 (토글)", description = "이미 북마크한 장소면 북마크를 취소하고, 아니면 북마크합니다.")
    @PostMapping("/{placeId}/bookmark")
    public ResponseEntity<ApiResponse<PlaceBookmarkResponseDto>> toggleBookmark(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long placeId) {

        PlaceBookmarkResponseDto response = placeBookmarkService.toggleBookmark(userDetails.getUserId(), placeId);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }
}
