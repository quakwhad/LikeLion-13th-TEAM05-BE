package com.likelion.artipick.interestcategory.application;

import com.likelion.artipick.category.domain.Category;
import com.likelion.artipick.category.domain.repository.CategoryRepository;
import com.likelion.artipick.global.code.status.ErrorStatus;
import com.likelion.artipick.global.exception.GeneralException;
import com.likelion.artipick.interestcategory.api.dto.request.InterestCategoryRequestDto;
import com.likelion.artipick.interestcategory.domain.InterestCategory;
import com.likelion.artipick.interestcategory.domain.repository.InterestCategoryRepository;
import com.likelion.artipick.user.domain.User;
import com.likelion.artipick.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InterestCategoryService {

    private final InterestCategoryRepository interestCategoryRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public List<String> updateInterestCategories(Long userId, InterestCategoryRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // Get current interested categories for the user
        List<InterestCategory> currentInterestCategories = interestCategoryRepository.findByUserId(userId);
        Set<String> currentCategoryNames = currentInterestCategories.stream()
                .map(ic -> ic.getCategory().getName().getDisplayName())
                .collect(Collectors.toSet());

        // Get new interested category names from the request
        Set<String> newCategoryNames = requestDto.categoryNames().stream().collect(Collectors.toSet());

        // Categories to add
        List<String> categoryNamesToAdd = newCategoryNames.stream()
                .filter(newName -> !currentCategoryNames.contains(newName))
                .collect(Collectors.toList());

        // Categories to remove
        List<InterestCategory> interestCategoriesToRemove = currentInterestCategories.stream()
                .filter(ic -> !newCategoryNames.contains(ic.getCategory().getName()))
                .collect(Collectors.toList());

        // Remove old categories
        if (!interestCategoriesToRemove.isEmpty()) {
            interestCategoryRepository.deleteAll(interestCategoriesToRemove);
        }

        // Add new categories
        for (String categoryName : categoryNamesToAdd) {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.CATEGORY_NOT_FOUND));

            InterestCategory interestCategory = InterestCategory.builder()
                    .user(user)
                    .category(category)
                    .build();
            interestCategoryRepository.save(interestCategory);
        }

        return newCategoryNames.stream().toList();
    }
}
