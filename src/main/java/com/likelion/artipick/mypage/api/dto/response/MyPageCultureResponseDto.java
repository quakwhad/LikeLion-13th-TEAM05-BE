package com.likelion.artipick.mypage.api.dto.response;

import com.likelion.artipick.culture.domain.Culture;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "내가 찜한 문화생활 목록 조회 응답 DTO")
public record MyPageCultureResponseDto(
    @Schema(description = "문화 ID", example = "1")
    Long cultureId,
    @Schema(description = "제목", example = "패트릭 블랑: 수직정원")
    String title,
    @Schema(description = "포스터 이미지 URL", example = "http://www.culture.go.kr/upload/rdf/1.jpg")
    String imgUrl,
    @Schema(description = "장소", example = "부산현대미술관")
    String place,
    @Schema(description = "카테고리", example = "전시")
    String category
) {
    public static MyPageCultureResponseDto from(Culture culture) {
        return new MyPageCultureResponseDto(
            culture.getId(),
            culture.getTitle(),
            culture.getImgUrl(),
            culture.getPlace(),
            culture.getCategory().getDisplayName()
        );
    }
}