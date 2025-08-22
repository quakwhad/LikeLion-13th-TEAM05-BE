package com.likelion.artipick.interestcategory.domain.repository;

import com.likelion.artipick.interestcategory.domain.InterestCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestCategoryRepository extends JpaRepository<InterestCategory, Long> {
    List<InterestCategory> findByUserId(Long userId);
}
