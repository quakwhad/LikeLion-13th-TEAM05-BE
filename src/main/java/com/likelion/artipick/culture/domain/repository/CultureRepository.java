package com.likelion.artipick.culture.domain.repository;

import com.likelion.artipick.culture.domain.Category;
import com.likelion.artipick.culture.domain.Culture;
import com.likelion.artipick.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CultureRepository extends JpaRepository<Culture, Long> {

    Optional<Culture> findBySeq(String seq);

    boolean existsBySeq(String seq);

    Page<Culture> findByUser(User user, Pageable pageable);

    Page<Culture> findByIsFromApi(Boolean isFromApi, Pageable pageable);

    @Query("SELECT c FROM Culture c WHERE " +
           "(:keyword IS NULL OR c.title LIKE %:keyword% OR c.contents LIKE %:keyword%) AND " +
           "(:area IS NULL OR c.area LIKE %:area%) AND " +
           "(:sigungu IS NULL OR c.sigungu LIKE %:sigungu%) AND " +
           "(:categories IS NULL OR c.category IN :categories)")
    Page<Culture> searchBy(@Param("keyword") String keyword,
                           @Param("area") String area,
                           @Param("sigungu") String sigungu,
                           @Param("categories") java.util.List<Category> categories,
                           Pageable pageable);

    List<Culture> findByEndDateGreaterThanEqualOrderByViewCountDescLikeCountDesc(LocalDate date);

    List<Culture> findByIdInAndEndDateGreaterThanEqual(List<Long> ids, LocalDate date);
}
