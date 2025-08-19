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
}
