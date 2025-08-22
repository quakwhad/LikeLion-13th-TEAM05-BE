package com.likelion.artipick.culture.api.dto.response;

import com.likelion.artipick.culture.domain.Category;
import com.likelion.artipick.culture.domain.Culture;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "문화행사 목록 조회 응답")
public record CultureListResponse(
        @Schema(description = "문화행사 ID", example = "1")
        Long id,

        @Schema(description = "문화행사 제목", example = "패트릭 블랑: 수직정원")
        String title,

        @Schema(description = "시작일", example = "2018-06-16")
        LocalDate startDate,

        @Schema(description = "종료일", example = "2028-12-31")
        LocalDate endDate,

        @Schema(description = "장소명", example = "부산현대미술관")
        String place,

        @Schema(description = "카테고리", example = "EXHIBITION")
        Category category,

        @Schema(description = "지역", example = "부산")
        String area,

        @Schema(description = "가격 정보", example = "무료")
        String price,

        @Schema(description = "이미지 URL", example = "http://www.culture.go.kr/upload/rdf/23/07/show_202307716201843746.jpg")
        String imgUrl,

        @Schema(description = "API 데이터 여부", example = "true")
        Boolean isFromApi,

        @Schema(description = "좋아요 수", example = "12")
        int likeCount
) {
    public static CultureListResponse from(Culture culture) {
        return new CultureListResponse(
                culture.getId(),
                culture.getTitle(),
                culture.getStartDate(),
                culture.getEndDate(),
                culture.getPlace(),
                culture.getCategory(),
                culture.getArea(),
                culture.getPrice(),
                culture.getImgUrl(),
                culture.getIsFromApi(),
                culture.getLikeCount()
        );
    }
}
