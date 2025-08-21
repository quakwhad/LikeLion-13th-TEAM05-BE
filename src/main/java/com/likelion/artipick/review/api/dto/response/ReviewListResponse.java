package com.likelion.artipick.review.api.dto.response;

import com.likelion.artipick.review.domain.Review;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "리뷰 목록 응답 DTO")
public record ReviewListResponse(

        @Schema(description = "리뷰 ID", example = "1")
        Long id,

        @Schema(description = "리뷰 내용", example = "정말 즐거운 축제")
        String content,

        @Schema(description = "평점 (1~5)", example = "5")
        int rating,

        @Schema(description = "작성자 닉네임", example = "username")
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
