package com.likelion.artipick.place.application;

import com.likelion.artipick.global.code.status.ErrorStatus;
import com.likelion.artipick.global.exception.GeneralException;
import com.likelion.artipick.place.domain.Place;
import com.likelion.artipick.place.domain.PlaceBookmark;
import com.likelion.artipick.place.api.dto.response.PlaceBookmarkResponseDto;
import com.likelion.artipick.place.domain.repository.PlaceBookmarkRepository;
import com.likelion.artipick.place.domain.repository.PlaceRepository;
import com.likelion.artipick.user.domain.User;
import com.likelion.artipick.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceBookmarkService {

    private final PlaceBookmarkRepository placeBookmarkRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    @Transactional
    public PlaceBookmarkResponseDto toggleBookmark(Long userId, Long placeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PLACE_NOT_FOUND));

        Optional<PlaceBookmark> bookmarkOpt = placeBookmarkRepository.findByUserAndPlace(user, place);

        if (bookmarkOpt.isPresent()) {
            placeBookmarkRepository.delete(bookmarkOpt.get());
            return new PlaceBookmarkResponseDto(false, "장소 북마크를 취소했습니다.");
        } else {
            PlaceBookmark newBookmark = PlaceBookmark.builder()
                    .user(user)
                    .place(place)
                    .build();
            placeBookmarkRepository.save(newBookmark);
            return new PlaceBookmarkResponseDto(true, "장소를 북마크했습니다.");
        }
    }
}
