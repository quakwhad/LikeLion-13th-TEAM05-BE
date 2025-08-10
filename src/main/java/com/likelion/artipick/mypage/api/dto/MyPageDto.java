package com.likelion.artipick.mypage.api.dto;

import com.likelion.artipick.place.domain.Place;
import com.likelion.artipick.post.domain.Post;
import com.likelion.artipick.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
@AllArgsConstructor
public class MyPageDto {
    @Schema(description = "유저 정보")
    private MyPageUserDto user;
    @Schema(description = "유저가 작성한 게시글")
    private Page<MyPagePostDto> posts;
    @Schema(description = "유저가 좋아요한 게시글")
    private Page<MyPagePostDto> likedPosts;
    @Schema(description = "유저가 북마크한 장소")
    private Page<MyPagePlaceDto> bookmarkedPlaces;

    public static MyPageDto of(User user, Page<Post> posts, Page<Post> likedPosts, Page<Place> bookmarkedPlaces) {
        return MyPageDto.builder()
                .user(MyPageUserDto.from(user))
                .posts(posts.map(MyPagePostDto::from))
                .likedPosts(likedPosts.map(MyPagePostDto::from))
                .bookmarkedPlaces(bookmarkedPlaces.map(MyPagePlaceDto::from))
                .build();
    }
}
