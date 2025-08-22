package com.likelion.artipick.review.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "리뷰 작성/수정 요청 DTO")
public record ReviewRequest(
        @Schema(description = "리뷰 내용", example = "정말 즐거운 축제")
        String content,

        @Schema(description = "평점", example = "5")
        int rating,

        @Schema(description = "게시글 ID", example = "1")
        Long cultureId
) {}
