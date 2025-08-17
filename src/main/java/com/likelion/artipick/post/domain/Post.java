package com.likelion.artipick.post.domain;

import com.likelion.artipick.category.domain.Category;
import com.likelion.artipick.place.domain.Place;
import com.likelion.artipick.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 게시글 정보를 담는 엔티티
// 데이터베이스의 'posts' 테이블과 연결
@Table(name = "post")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    // Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 제목
    private String title;

    // 시작일
    @Column(name = "start_date")
    private String startDate;

    // 종료일
    @Column(name = "end_date")
    private String endDate;

    // 가격
    private String price;

    // 내용
    private String content;

    // 문의 번호
    private String phone;

    // 이미지 URL
    @Column(name = "img_url")
    private String imgUrl;

    // 장소 URL
    @Column(name = "place_url")
    private String placeUrl;

    // 일련번호
    private String seq;

    // 관련 URL
    private String url;

    // 카테고리 Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id") // DB의 'category_id' 컬럼과 매핑됨
    private Category category; // ✨ Category 객체를 직접 참조

    // 경도
    @Column(name = "gps_x")
    private String gpsX;

    // 위도
    @Column(name = "gps_y")
    private String gpsY;

    // 생성 일시
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 수정 일시
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 상태
    private String status;

    // 조회수
    @Column(name = "view_count")
    private Integer viewCount;

    // 좋아요(찜하기) 수
    @Column(name = "like_count")
    private Integer likeCount;

    // 댓글 수
    @Column(name = "comment_count")
    private Integer commentCount;

    // 한 명의 사용자가 여러 개의 게시글을 작성 가능 (ManyToOne)
    // 게시글은 한 명의 사용자에게 속함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 하나의 장소에 여러 게시글이 속할 수 있음 (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @Builder
    public Post(String title, String startDate, String endDate, String price, String content, String phone, String imgUrl, String placeUrl, String seq, String url, Category category, String gpsX, String gpsY, LocalDateTime createdAt, LocalDateTime updatedAt, String status, Integer viewCount, Integer likeCount, Integer commentCount, User user, Place place) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.content = content;
        this.phone = phone;
        this.imgUrl = imgUrl;
        this.placeUrl = placeUrl;
        this.seq = seq;
        this.url = url;
        this.category = category;
        this.gpsX = gpsX;
        this.gpsY = gpsY;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.user = user;
        this.place = place;
    }
}
