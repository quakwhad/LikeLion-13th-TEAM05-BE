package com.likelion.artipick.category.domain.repository;

import com.likelion.artipick.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 카테고리 이름으로 DB에 존재하는지 확인하기 위한 메서드
    Optional<Category> findByName(String name);
}