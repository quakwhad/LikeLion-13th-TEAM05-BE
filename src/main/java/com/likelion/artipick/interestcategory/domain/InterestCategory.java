package com.likelion.artipick.interestcategory.domain;

import com.likelion.artipick.culture.domain.Category; // Category Enum 클래스 import
import com.likelion.artipick.global.entity.BaseEntity;
import com.likelion.artipick.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "interest_categories")
public class InterestCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_name", nullable = false)
    private Category category; // Category Enum 타입 필드 추가

    @Builder
    public InterestCategory(Long id, User user, Category category) {
        this.id = id;
        this.user = user;
        this.category = category;
    }
}

