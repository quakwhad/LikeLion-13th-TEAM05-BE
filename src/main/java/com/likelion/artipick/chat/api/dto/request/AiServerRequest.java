package com.likelion.artipick.chat.api.dto.request;

import com.likelion.artipick.culture.domain.Culture;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "AI 서버 요청 (내부용)")
public record AiServerRequest(
        @Schema(description = "사용자 메시지", example = "오늘 서울 전시 추천해줘")
        String message,

        @Schema(description = "문화행사 목록")
        List<CultureInfo> cultures,

        @Schema(description = "타임스탬프", example = "2025-01-15T10:30:00")
        String timestamp
) {
    public static AiServerRequest of(Long userId, String message, List<Culture> cultures) {
        List<CultureInfo> cultureInfos = cultures.stream()
                .map(CultureInfo::from)
                .toList();
        
        return new AiServerRequest(message, cultureInfos, LocalDateTime.now().toString());
    }

    @Schema(description = "문화행사 정보")
    public record CultureInfo(
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
        public static CultureInfo from(Culture culture) {
            return new CultureInfo(
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
}
