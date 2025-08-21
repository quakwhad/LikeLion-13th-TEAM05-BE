package com.likelion.artipick.review.application;

import com.likelion.artipick.culture.domain.Culture;
import com.likelion.artipick.culture.domain.repository.CultureRepository;
import com.likelion.artipick.global.code.status.ErrorStatus;
import com.likelion.artipick.global.exception.GeneralException;
import com.likelion.artipick.review.api.dto.request.ReviewRequest;
import com.likelion.artipick.review.api.dto.response.ReviewResponse;
import com.likelion.artipick.review.domain.Review;
import com.likelion.artipick.review.domain.repository.ReviewRepository;
import com.likelion.artipick.user.domain.User;
import com.likelion.artipick.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본은 조회 전용
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CultureRepository cultureRepository;
    private final UserRepository userRepository; // 추가

    // 공통적으로 User 조회
    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
    }

    // 리뷰 생성
    @Transactional
    public ReviewResponse createReview(ReviewRequest request, Long userId) {
        User user = getUserOrThrow(userId);
        Culture culture = cultureRepository.findById(request.cultureId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESOURCE_NOT_FOUND));

        Review review = new Review(request.content(), request.rating(), user, culture);
        Review saved = reviewRepository.save(review);

        return ReviewResponse.from(saved);
    }

    // 리뷰 단건 조회
    public ReviewResponse getReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESOURCE_NOT_FOUND));
        review.increaseView();
        return ReviewResponse.from(review);
    }

    // 특정 문화 컨텐츠의 리뷰 조회
    public Page<Review> getReviewsByCulture(Long cultureId, Pageable pageable) {
        Culture culture = cultureRepository.findById(cultureId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESOURCE_NOT_FOUND));

        return reviewRepository.findByCulture(culture, pageable);
    }

    // 특정 유저의 리뷰 조회
    public Page<Review> getReviewsByUser(Long userId, Pageable pageable) {
        User user = getUserOrThrow(userId);
        return reviewRepository.findByUser(user, pageable);
    }

    // 리뷰 수정
    @Transactional
    public ReviewResponse updateReview(Long id, ReviewRequest request, Long userId) {
        User user = getUserOrThrow(userId);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESOURCE_NOT_FOUND));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new GeneralException(ErrorStatus.FORBIDDEN);
        }

        review.update(request.content(), request.rating());
        return ReviewResponse.from(review);
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long id, Long userId) {
        User user = getUserOrThrow(userId);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESOURCE_NOT_FOUND));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new GeneralException(ErrorStatus.FORBIDDEN);
        }

        reviewRepository.delete(review);
    }
}
