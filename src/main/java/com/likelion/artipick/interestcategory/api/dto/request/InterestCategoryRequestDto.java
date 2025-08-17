package com.likelion.artipick.interestcategory.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "사용자의 관심 카테고리를 등록/수정하는 요청 DTO")
public record InterestCategoryRequestDto(
        @Schema(description = "관심 카테고리 이름 목록", example = "[\"미술\", \"음악\", \"사진\"]")
        List<String> categoryNames
) {}

