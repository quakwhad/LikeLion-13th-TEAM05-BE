package com.likelion.artipick.mypage.application;

import com.likelion.artipick.global.code.status.ErrorStatus;
import com.likelion.artipick.global.exception.GeneralException;

import com.likelion.artipick.mypage.api.dto.response.MyPagePlaceResponseDto;
import com.likelion.artipick.mypage.api.dto.response.MyPageUserResponseDto;
import com.likelion.artipick.mypage.api.dto.request.ProfileUpdateRequestDto;
import com.likelion.artipick.place.domain.repository.PlaceBookmarkRepository;
import com.likelion.artipick.user.domain.User;
import com.likelion.artipick.user.domain.repository.UserRepository;
import com.likelion.artipick.interestcategory.domain.repository.InterestCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final UserRepository userRepository;
    private final PlaceBookmarkRepository placeBookmarkRepository;
    private final InterestCategoryRepository interestCategoryRepository;

    public MyPageUserResponseDto getMyPageInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        List<String> interestedCategories = interestCategoryRepository.findByUserId(userId)
                .stream()
                .map(interestCategory -> interestCategory.getCategory().getDisplayName())
                .collect(Collectors.toList());

        return MyPageUserResponseDto.from(user, interestedCategories);
    }

    public Page<MyPagePlaceResponseDto> getBookmarkedPlaces(Long userId, Pageable pageable) {
        return placeBookmarkRepository.findBookmarkedPlacesByUserId(userId, pageable)
                .map(MyPagePlaceResponseDto::from);
    }

    @Transactional
    public void updateProfile(Long userId, ProfileUpdateRequestDto profileUpdateRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        user.updateProfile(profileUpdateRequestDto.nickname(), profileUpdateRequestDto.introduction());
    }
}