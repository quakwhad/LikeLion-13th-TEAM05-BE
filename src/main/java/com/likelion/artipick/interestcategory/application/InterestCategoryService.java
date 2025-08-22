package com.likelion.artipick.interestcategory.application;

import com.likelion.artipick.culture.domain.Category;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InterestCategoryService {

    private final InterestCategoryRepository interestCategoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<String> updateInterestCategories(Long userId, InterestCategoryRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        interestCategoryRepository.deleteAll(interestCategoryRepository.findByUserId(userId));

        List<InterestCategory> newInterestCategories = requestDto.categoryNames().stream()
                .map(name -> {
                    try {
                        return InterestCategory.builder()
                                .user(user)
                                .category(Category.fromDisplayName(name))
                                .build();
                    } catch (IllegalArgumentException e) {
                        throw new GeneralException(ErrorStatus.CATEGORY_NOT_FOUND);
                    }
                })
                .collect(Collectors.toList());

        interestCategoryRepository.saveAll(newInterestCategories);

        return newInterestCategories.stream()
                .map(interestCategory -> interestCategory.getCategory().getDisplayName())
                .collect(Collectors.toList());
    }
}
