package com.likelion.artipick.like.api;

import com.likelion.artipick.global.code.dto.ApiResponse;
import com.likelion.artipick.global.security.CustomUserDetails;
import com.likelion.artipick.like.api.dto.response.LikeResponseDto;
import com.likelion.artipick.like.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "찜하기 API", description = "게시글 찜하기/취소")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "게시글 찜하기/취소 (토글)", description = "이미 찜한 게시글이면 찜하기를 취소하고, 아니면 찜합니다.")
    @PostMapping("/{postId}/like")
    public ResponseEntity<ApiResponse<LikeResponseDto>> toggleLike(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId) {

        LikeResponseDto response = likeService.toggleLike(userDetails.getUserId(), postId);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }
}
