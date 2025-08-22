package com.likelion.artipick.review.domain;

import com.likelion.artipick.global.entity.BaseAuditEntity;
import com.likelion.artipick.user.domain.User;
import com.likelion.artipick.culture.domain.Culture;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "reviews")
public class Review extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private int rating;

    @Column(name = "like_count", nullable = false)
    private int likeCount = 0;

    @Column(name = "view_count", nullable = false)
    private int viewCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "culture_id", nullable = false)
    private Culture culture;

    public Review(String content, int rating, User user, Culture culture) {
        this.content = content;
        this.rating = rating;
        this.user = user;
        this.culture = culture;
    }

    public void update(String content, int rating) {
        if (content != null && !content.isBlank()) {
            this.content = content;
        }
        this.rating = rating;
    }

    public void increaseLike() {
        this.likeCount++;
    }

    public void decreaseLike() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public void increaseView() {
        this.viewCount++;
    }
}
