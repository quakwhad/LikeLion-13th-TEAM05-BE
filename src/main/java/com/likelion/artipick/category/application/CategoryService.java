package com.likelion.artipick.category.application;

import com.likelion.artipick.category.domain.CategoryList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    public List<String> getCategoryList() {
        return Arrays.stream(CategoryList.values())
                .map(CategoryList::getDisplayName)
                .collect(Collectors.toList());
    }
}
