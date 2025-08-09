package com.likelion.artipick.like.domain;

import com.likelion.artipick.post.domain.Post;
import com.likelion.artipick.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// '찜하기' 정보를 담는 엔티티
// 누가(User) 어떤 게시글(Post)에 찜하기를 눌렀는지 기록
// 좋아요는 게시글과 사용자 간의 관계를 나타내기 때문에, 중간 테이블(likes)로 설계
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 한 명의 사용자는 여러 개의 '찜하기'를 누를 수 있음 (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    // 한 개의 게시글은 여러 명의 사용자에게 '찜하기'를 받을 수 있음 (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Like(User user, Post post) {
        this.user = user;
        this.post = post;
    }
}