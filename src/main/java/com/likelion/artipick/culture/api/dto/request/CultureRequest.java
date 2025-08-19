package com.likelion.artipick.culture.api.dto.request;

import com.likelion.artipick.culture.domain.Category;
import java.time.LocalDate;

public record CultureRequest(
        String title,
        LocalDate startDate,
        LocalDate endDate,
        String place,
        Category category,
        String area,
        String sigungu,
        String price,
        String placeAddr,
        String gpsX,
        String gpsY,
        String placeUrl,
        String phone,
        String contents,
        String imgUrl
) {}
