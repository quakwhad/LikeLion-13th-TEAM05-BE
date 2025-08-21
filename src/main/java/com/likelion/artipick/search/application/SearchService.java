package com.likelion.artipick.search.application;

import com.likelion.artipick.culture.domain.Category;
import com.likelion.artipick.culture.domain.Culture;
import com.likelion.artipick.culture.domain.repository.CultureRepository;
import com.likelion.artipick.global.code.status.ErrorStatus;
import com.likelion.artipick.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    private final CultureRepository cultureRepository;

    public Page<Culture> searchCultures(String keyword, String area, String sigungu, String category, Pageable pageable) {
        java.util.List<Category> categoryEnums = null;
        if (category != null && !category.isBlank()) {
            try {
                categoryEnums = java.util.Arrays.stream(category.split(","))
                        .map(String::trim)
                        .map(Category::fromDisplayName)
                        .collect(java.util.stream.Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new GeneralException(ErrorStatus.CATEGORY_NOT_FOUND);
            }
        }
        return cultureRepository.searchBy(keyword, area, sigungu, categoryEnums, pageable);
    }
}
