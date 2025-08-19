package com.likelion.artipick.mypage.api.dto.response;

import com.likelion.artipick.place.domain.Place;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "마이페이지의 개별 북마크 장소 정보 응답 DTO")
public record MyPagePlaceResponseDto(
        @Schema(description = "장소 ID")
        Long id,
        @Schema(description = "장소명")
        String name,
        @Schema(description = "주소")
        String address
) {
    public static MyPagePlaceResponseDto from(Place place) {
        return new MyPagePlaceResponseDto(
                place.getId(),
                place.getName(),
                place.getAddress()
        );
    }
}