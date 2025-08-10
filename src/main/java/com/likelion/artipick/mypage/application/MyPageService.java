package com.likelion.artipick.mypage.application;

import com.likelion.artipick.global.code.status.ErrorStatus;
import com.likelion.artipick.global.exception.GeneralException;

import com.likelion.artipick.like.domain.repository.LikeRepository;
import com.likelion.artipick.mypage.api.dto.MyPagePlaceDto;
import com.likelion.artipick.mypage.api.dto.MyPagePostDto;
import com.likelion.artipick.mypage.api.dto.ProfileUpdateDto;
import com.likelion.artipick.place.domain.repository.PlaceBookmarkRepository;
import com.likelion.artipick.user.domain.User;
import com.likelion.artipick.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final PlaceBookmarkRepository placeBookmarkRepository;

    @Transactional(readOnly = true)
    public Page<MyPagePostDto> getLikedPosts(Long userId, Pageable pageable) {
        return likeRepository.findLikedPostsByUserId(userId, pageable)
                .map(MyPagePostDto::from);
    }

    @Transactional(readOnly = true)
    public Page<MyPagePlaceDto> getBookmarkedPlaces(Long userId, Pageable pageable) {
        return placeBookmarkRepository.findBookmarkedPlacesByUserId(userId, pageable)
                .map(MyPagePlaceDto::from);
    }

    @Transactional
    public void updateProfile(Long userId, ProfileUpdateDto profileUpdateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        user.updateProfile(profileUpdateDto.getNickname(), profileUpdateDto.getIntroduction());
    }
}
