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

    public Page<Culture> searchCultures(String keyword, String location, String category, Pageable pageable) {
        Category categoryEnum = null;
        if (category != null && !category.isEmpty()) {
            try {
                categoryEnum = Category.fromDisplayName(category);
            } catch (IllegalArgumentException e) {
                throw new GeneralException(ErrorStatus.CATEGORY_NOT_FOUND);
            }
        }
        return cultureRepository.searchBy(keyword, location, categoryEnum, pageable);
    }
}
