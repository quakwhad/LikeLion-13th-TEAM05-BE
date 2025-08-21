package com.likelion.artipick.review.api.dto;

import com.likelion.artipick.review.api.dto.request.ReviewRequest;
import com.likelion.artipick.review.api.dto.response.ReviewListResponse;
import com.likelion.artipick.review.api.dto.response.ReviewResponse;
import com.likelion.artipick.review.application.ReviewService;
import com.likelion.artipick.global.code.dto.ApiResponse;
import com.likelion.artipick.global.code.status.ErrorStatus;
import com.likelion.artipick.global.exception.GeneralException;
import com.likelion.artipick.global.security.CustomUserDetails;
import com.likelion.artipick.user.domain.User;
import com.likelion.artipick.user.domain.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "리뷰", description = "리뷰 관련 API")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 작성")
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @RequestBody ReviewRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(ApiResponse.onSuccess(
                reviewService.createReview(request, userDetails.getUserId())));
    }

    @Operation(summary = "문화 컨텐츠 리뷰 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ReviewListResponse>>> getReviewsByCulture(
            @RequestParam Long cultureId,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<ReviewListResponse> reviews = reviewService.getReviewsByCulture(cultureId, pageable)
                .map(ReviewListResponse::from);

        return ResponseEntity.ok(ApiResponse.onSuccess(reviews));
    }

    @Operation(summary = "사용자가 작성한 리뷰 목록 조회")
    @GetMapping("/user")
    public ResponseEntity<ApiResponse<Page<ReviewListResponse>>> getReviewsByUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<ReviewListResponse> reviews = reviewService.getReviewsByUser(userDetails.getUserId(), pageable)
                .map(ReviewListResponse::from);

        return ResponseEntity.ok(ApiResponse.onSuccess(reviews));
    }

    @Operation(summary = "리뷰 상세 조회")
    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getReview(
            @PathVariable Long reviewId) {

        return ResponseEntity.ok(ApiResponse.onSuccess(
                reviewService.getReview(reviewId)));
    }

    @Operation(summary = "리뷰 수정")
    @PatchMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(ApiResponse.onSuccess(
                reviewService.updateReview(reviewId, request, userDetails.getUserId())));
    }

    @Operation(summary = "리뷰 삭제")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        reviewService.deleteReview(reviewId, userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }
}

