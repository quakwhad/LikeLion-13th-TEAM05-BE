package com.likelion.artipick.recommendation.client.dto;

import com.likelion.artipick.culture.domain.Culture;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AI 전송용 문화행사 데이터")
public record AiCultureDto(
        @Schema(description = "ID")
        Long id,
        
        @Schema(description = "제목")
        String title,
        
        @Schema(description = "내용")
        String contents,
        
        @Schema(description = "지역")
        String area,
        
        @Schema(description = "시군구")
        String sigungu,
        
        @Schema(description = "카테고리")
        String category,
        
        @Schema(description = "시작일")
        String startDate,
        
        @Schema(description = "종료일")
        String endDate,
        
        @Schema(description = "조회수")
        int viewCount,
        
        @Schema(description = "좋아요 수")
        int likeCount
) {
    public static AiCultureDto from(Culture culture) {
        return new AiCultureDto(
                culture.getId(),
                culture.getTitle(),
                culture.getContents(),
                culture.getArea(),
                culture.getSigungu(),
                culture.getCategory() != null ? culture.getCategory().getDisplayName() : null,
                culture.getStartDate() != null ? culture.getStartDate().toString() : null,
                culture.getEndDate() != null ? culture.getEndDate().toString() : null,
                culture.getViewCount(),
                culture.getLikeCount()
        );
    }
}
