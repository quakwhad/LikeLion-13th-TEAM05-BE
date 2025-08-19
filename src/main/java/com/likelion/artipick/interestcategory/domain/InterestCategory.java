package com.likelion.artipick.interestcategory.domain;

import com.likelion.artipick.culture.domain.Category; // Category Enum 클래스 import
import com.likelion.artipick.global.entity.BaseEntity;
import com.likelion.artipick.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
}