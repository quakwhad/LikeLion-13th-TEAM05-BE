package com.likelion.artipick.culture.domain.repository;

import com.likelion.artipick.culture.domain.Culture;
import com.likelion.artipick.culture.domain.CultureLike;
import com.likelion.artipick.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CultureLikeRepository extends JpaRepository<CultureLike, Long> {
    Optional<CultureLike> findByUserAndCulture(User user, Culture culture);
}
