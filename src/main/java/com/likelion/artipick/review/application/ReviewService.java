package com.likelion.artipick.review.application;

import com.likelion.artipick.culture.domain.Culture;
import com.likelion.artipick.culture.domain.repository.CultureRepository;
import com.likelion.artipick.review.api.dto.request.ReviewRequest;
import com.likelion.artipick.review.api.dto.response.ReviewResponse;
import com.likelion.artipick.review.domain.Review;
import com.likelion.artipick.review.domain.repository.ReviewRepository;
import com.likelion.artipick.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CultureRepository cultureRepository;

    // 리뷰 생성
    public ReviewResponse createReview(ReviewRequest request, User user) {
        Culture culture = cultureRepository.findById(request.cultureId())
                .orElseThrow(() -> new RuntimeException("문화 컨텐츠 없음"));

        Review review = new Review(request.content(), request.rating(), user, culture);
        Review saved = reviewRepository.save(review);

        return ReviewResponse.from(saved);
    }

    // 리뷰 단건 조회
    @Transactional(readOnly = true)
    public ReviewResponse getReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("리뷰 없음"));
        review.increaseView(); // 조회수 증가
        return ReviewResponse.from(review);
    }

    // 특정 문화 컨텐츠의 리뷰 조회
    @Transactional(readOnly = true)
    public Page<Review> getReviewsByCulture(Long cultureId, Pageable pageable) {
        Culture culture = cultureRepository.findById(cultureId)
                .orElseThrow(() -> new RuntimeException("문화 컨텐츠 없음"));

        return reviewRepository.findByCulture(culture, pageable);
    }

    // 특정 유저의 리뷰 조회
    @Transactional(readOnly = true)
    public Page<Review> getReviewsByUser(User user, Pageable pageable) {
        return reviewRepository.findByUser(user, pageable);
    }

    // 리뷰 수정
    public ReviewResponse updateReview(Long id, ReviewRequest request, User user) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("리뷰 없음"));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("본인 리뷰만 수정할 수 있습니다.");
        }

        review.update(request.content(), request.rating());
        return ReviewResponse.from(review);
    }

    // 리뷰 삭제
    public void deleteReview(Long id, User user) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("리뷰 없음"));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("본인 리뷰만 삭제할 수 있습니다.");
        }

        reviewRepository.delete(review);
    }
}
