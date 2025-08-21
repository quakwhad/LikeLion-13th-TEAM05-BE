package com.likelion.artipick.review.api.dto.response;

import com.likelion.artipick.review.domain.Review;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "리뷰 상세 응답 DTO")
public record ReviewResponse(
        Long id,
        String content,
        int rating,
        String userName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getContent(),
                review.getRating(),
                review.getUser().getNickname(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}
