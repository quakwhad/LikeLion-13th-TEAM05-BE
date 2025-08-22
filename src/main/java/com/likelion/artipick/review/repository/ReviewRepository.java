package com.likelion.artipick.review.domain.repository;

import com.likelion.artipick.culture.domain.Culture;
import com.likelion.artipick.review.domain.Review;
import com.likelion.artipick.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = {"user", "culture"})
    Optional<Review> findById(Long id);

    @EntityGraph(attributePaths = {"user", "culture"})
    Page<Review> findByCulture(Culture culture, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "culture"})
    Page<Review> findByUser(User user, Pageable pageable);
}
