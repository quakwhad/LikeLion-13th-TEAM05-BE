package com.likelion.artipick.like.domain.repository;

import com.likelion.artipick.post.domain.Post;
import com.likelion.artipick.like.domain.Like;
import com.likelion.artipick.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    @Query("select l.post from Like l where l.user.id = :userId")
    Page<Post> findLikedPostsByUserId(@Param("userId") Long userId, Pageable pageable);

    Optional<Like> findByUserAndPost(User user, Post post);

    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);
}
