package com.likelion.artipick.culture.api.dto.request;

import com.likelion.artipick.culture.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "문화 행사 등록 요청 DTO")
public record CultureRequest(

        @Schema(description = "문화 행사 제목", example = "[경복궁] 2025년 경회루 특별관람")
        String title,

        @Schema(description = "문화 행사 시작일", example = "2025-05-08")
        LocalDate startDate,

        @Schema(description = "문화 행사 종료일", example = "2025-10-31")
        LocalDate endDate,

        @Schema(description = "행사 장소명", example = "경복궁 경회루")
        String place,

        @Schema(description = "문화 행사 카테고리 (교육/체험, 전시/공연 등)")
        Category category,

        @Schema(description = "행사 지역(광역시/도)", example = "서울특별시")
        String area,

        @Schema(description = "행사 시군구", example = "종로구")
        String sigungu,

        @Schema(description = "참가비/이용료", example = "무료")
        String price,

        @Schema(description = "행사 장소 주소", example = "서울특별시 종로구 사직로 161")
        String placeAddr,

        @Schema(description = "위도 좌표", example = "126.9769")
        String gpsX,

        @Schema(description = "경도 좌표", example = "37.5796")
        String gpsY,

        @Schema(description = "행사 관련 웹사이트 URL", example = "https://royalpalace.go.kr")
        String placeUrl,

        @Schema(description = "문의 전화번호", example = "02-3700-3900~1")
        String phone,

        @Schema(description = "행사 내용 설명", example = "경복궁 경회루에서 특별 관람을 진행합니다.")
        String contents,

        @Schema(description = "행사 대표 이미지 URL", example = "https://example.com/images/event.jpg")
        String imgUrl
) {}
