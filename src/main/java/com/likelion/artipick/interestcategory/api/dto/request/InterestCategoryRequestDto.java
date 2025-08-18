package com.likelion.artipick.interestcategory.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "사용자의 관심 카테고리를 등록/수정하는 요청 DTO")
public record InterestCategoryRequestDto(
        @Schema(description = "관심 카테고리 이름 목록", example = "[\"전시\", \"연극\", \"교육/체험\", \"뮤지컬/오페라\", \"음악/콘서트\", \"국악\", \"행사/축제\", \"무용/발레\", \"아동/가족\"]")
        List<String> categoryNames
) {}