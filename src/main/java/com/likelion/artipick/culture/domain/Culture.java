package com.likelion.artipick.culture.domain;

import com.likelion.artipick.global.entity.BaseAuditEntity;
import com.likelion.artipick.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "culture")
public class Culture extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "seq", unique = true)
    private String seq;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "place", nullable = false, length = 100)
    private String place;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "area", nullable = false, length = 50)
    private String area;

    @Column(name = "sigungu", length = 50)
    private String sigungu;

    @Column(name = "price", length = 100)
    private String price;

    @Column(name = "place_addr", length = 255)
    private String placeAddr;

    @Column(name = "gps_x", length = 255)
    private String gpsX;

    @Column(name = "gps_y", length = 255)
    private String gpsY;

    @Column(name = "place_url", length = 500)
    private String placeUrl;

    @Column(name = "phone", length = 50)
    private String phone;

    @Lob
    @Column(name = "contents")
    private String contents;

    @Column(name = "img_url", length = 500)
    private String imgUrl;

    @Column(name = "is_from_api", nullable = false)
    private Boolean isFromApi = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Culture(String seq, String title, LocalDate startDate, LocalDate endDate,
                   String place, Category category, String area, String sigungu,
                   String price, String placeAddr, String gpsX, String gpsY,
                   String placeUrl, String phone, String contents, String imgUrl,
                   Boolean isFromApi, User user) {
        this.seq = seq;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.place = place;
        this.category = category;
        this.area = area;
        this.sigungu = sigungu;
        this.price = price;
        this.placeAddr = placeAddr;
        this.gpsX = gpsX;
        this.gpsY = gpsY;
        this.placeUrl = placeUrl;
        this.phone = phone;
        this.contents = contents;
        this.imgUrl = imgUrl;
        this.isFromApi = isFromApi != null ? isFromApi : true;
        this.user = user;
    }

    public void updateCulture(String title, LocalDate startDate, LocalDate endDate,
                              String place, Category category, String area, String sigungu,
                              String price, String placeAddr, String gpsX, String gpsY,
                              String placeUrl, String phone, String contents, String imgUrl)
    {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.place = place;
        this.category = category;
        this.area = area;
        this.sigungu = sigungu;
        this.price = price;
        this.placeAddr = placeAddr;
        this.gpsX = gpsX;
        this.gpsY = gpsY;
        this.placeUrl = placeUrl;
        this.phone = phone;
        this.contents = contents;
        this.imgUrl = imgUrl;
    }
}
