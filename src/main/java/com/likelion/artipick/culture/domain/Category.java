package com.likelion.artipick.culture.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    EXHIBITION("전시"),
    THEATER("연극"),
    EDUCATION("교육/체험"),
    MUSICAL("뮤지컬/오페라"),
    MUSIC("음악/콘서트"),
    TRADITIONAL("국악"),
    FESTIVAL("행사/축제"),
    DANCE("무용/발레"),
    FAMILY("아동/가족"),
    ETC("기타");

    private final String displayName;

    /**
     한글 표시 이름(displayName)을 기반으로 해당하는 Category 열거형 상수를 찾습니다.
     이 메서드는 외부(예: 클라이언트 요청)에서 문자열 형태로 카테고리 이름을 받았을 때,
     내부의 열거형 타입으로 변환하는 데 사용됩니다.

     @param displayName 찾고자 하는 카테고리의 한글 표시 이름 (예: "전시", "연극")
     @return 주어진 displayName과 일치하는 Category 열거형 상수
     @throws IllegalArgumentException 주어진 displayName과 일치하는 카테고리가 없을 경우 발생
     */
    public static Category fromDisplayName(String displayName) {
        for (Category category : Category.values()) {
            if (category.displayName.equals(displayName)) {
                return category;
            }
        }
        // 대소문자 구분 없이 영어 이름으로도 찾아보기
        try {
            return Category.valueOf(displayName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(displayName + "에 해당하는 카테고리를 찾을 수 없습니다.");
        }
    }
}
