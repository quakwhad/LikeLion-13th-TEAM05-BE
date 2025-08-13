package com.likelion.artipick.post.domain.repository;

import com.likelion.artipick.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    @Query("SELECT DISTINCT p.categoryId FROM Post p WHERE p.categoryId IS NOT NULL")
    List<Long> findDistinctCategoryId();
}
