package com.likelion.artipick.mypage.api.dto.response;

import com.likelion.artipick.place.domain.Place;
import com.likelion.artipick.post.domain.Post;
import com.likelion.artipick.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

@Schema(description = "마이페이지 전체 조회 응답 DTO")
public record MyPageResponseDto(
        @Schema(description = "유저 정보")
        MyPageUserResponseDto user,
        @Schema(description = "유저가 작성한 게시글")
        Page<MyPagePostResponseDto> posts,
        @Schema(description = "유저가 좋아요한 게시글")
        Page<MyPagePostResponseDto> likedPosts,
        @Schema(description = "유저가 북마크한 장소")
        Page<MyPagePlaceResponseDto> bookmarkedPlaces
) {
    public static MyPageResponseDto of(User user, Page<Post> posts, Page<Post> likedPosts, Page<Place> bookmarkedPlaces) {
        return new MyPageResponseDto(
                MyPageUserResponseDto.from(user),
                posts.map(MyPagePostResponseDto::from),
                likedPosts.map(MyPagePostResponseDto::from),
                bookmarkedPlaces.map(MyPagePlaceResponseDto::from)
        );
    }
}
