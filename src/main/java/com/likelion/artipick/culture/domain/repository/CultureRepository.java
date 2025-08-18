package com.likelion.artipick.culture.domain.repository;

import com.likelion.artipick.culture.domain.Culture;
import com.likelion.artipick.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CultureRepository extends JpaRepository<Culture, Long> {

    Optional<Culture> findBySeq(String seq);

    boolean existsBySeq(String seq);

    Page<Culture> findByUser(User user, Pageable pageable);

    Page<Culture> findByIsFromApi(Boolean isFromApi, Pageable pageable);
}
