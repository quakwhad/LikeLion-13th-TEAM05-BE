package com.likelion.artipick.place.domain.repository;

import com.likelion.artipick.place.domain.Place;
import com.likelion.artipick.place.domain.PlaceBookmark;
import com.likelion.artipick.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlaceBookmarkRepository extends JpaRepository<PlaceBookmark, Long> {
    Optional<PlaceBookmark> findByUserAndPlace(User user, Place place);

    @Query("""
    SELECT b.place
    FROM PlaceBookmark b
    WHERE b.user.id = :userId
    """)
    Page<Place> findBookmarkedPlacesByUserId(@Param("userId") Long userId, Pageable pageable);
}
