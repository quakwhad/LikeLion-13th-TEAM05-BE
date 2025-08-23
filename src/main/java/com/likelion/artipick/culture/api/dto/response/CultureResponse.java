package com.likelion.artipick.culture.api.dto.response;

import com.likelion.artipick.culture.domain.Category;
import com.likelion.artipick.culture.domain.Culture;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "문화행사 상세 조회 응답")
public record CultureResponse(
        @Schema(description = "문화행사 ID", example = "1")
        Long id,

        @Schema(description = "공공API 고유번호", example = "319005")
        String seq,

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

        @Schema(description = "시군구", example = "사하구")
        String sigungu,

        @Schema(description = "가격 정보", example = "무료")
        String price,

        @Schema(description = "장소 주소", example = "부산광역시 사하구 낙동남로 1191 부산 현대미술관")
        String placeAddr,

        @Schema(description = "장소 URL", example = "https://www.busan.go.kr/moca/index")
        String placeUrl,

        @Schema(description = "전화번호", example = "부산현대미술관 051-220-7400~1")
        String phone,

        @Schema(description = "상세내용")
        String contents,

        @Schema(description = "이미지 URL", example = "http://www.culture.go.kr/upload/rdf/23/07/show_202307716201843746.jpg")
        String imgUrl,

        @Schema(description = "API 데이터 여부", example = "true")
        Boolean isFromApi,

        @Schema(description = "좋아요 수", example = "12")
        int likeCount,

        @Schema(description = "조회수", example = "123")
        int viewCount

) {
    public static CultureResponse from(Culture culture) {
        return new CultureResponse(
                culture.getId(),
                culture.getSeq(),
                culture.getTitle(),
                culture.getStartDate(),
                culture.getEndDate(),
                culture.getPlace(),
                culture.getCategory(),
                culture.getArea(),
                culture.getSigungu(),
                culture.getPrice(),
                culture.getPlaceAddr(),
                culture.getPlaceUrl(),
                culture.getPhone(),
                culture.getContents(),
                culture.getImgUrl(),
                culture.getIsFromApi(),
                culture.getLikeCount(),
                culture.getViewCount()
        );
    }
}
