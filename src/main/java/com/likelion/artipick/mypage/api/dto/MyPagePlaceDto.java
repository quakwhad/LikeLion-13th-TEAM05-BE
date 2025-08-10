package com.likelion.artipick.mypage.api.dto;

import com.likelion.artipick.place.domain.Place;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MyPagePlaceDto {
    @Schema(description = "장소 ID")
    private Long id;
    @Schema(description = "장소명")
    private String name;
    @Schema(description = "주소")
    private String address;

    public static MyPagePlaceDto from(Place place) {
        return MyPagePlaceDto.builder()
                .id(place.getId())
                .name(place.getName())
                .address(place.getAddress())
                .build();
    }
}
