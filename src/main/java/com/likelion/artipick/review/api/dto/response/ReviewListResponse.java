package com.likelion.artipick.review.api.dto.response;

import com.likelion.artipick.review.domain.Review;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "리뷰 목록 응답 DTO")
public record ReviewListResponse(
        Long id,
        String content,
        int rating,
        String userName
) {
    public static ReviewListResponse from(Review review) {
        return new ReviewListResponse(
                review.getId(),
                review.getContent(),
                review.getRating(),
                review.getUser().getNickname()
        );
    }
}
