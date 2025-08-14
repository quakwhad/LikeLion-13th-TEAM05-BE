package com.likelion.artipick.mypage.api.dto.response;

import com.likelion.artipick.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "마이페이지에서 내가 쓰거나 찜한 게시글 목록의 개별 정보 응답 DTO")
public record MyPagePostResponseDto(
        @Schema(description = "게시글 아이디", example = "1")
        Long id,
        @Schema(description = "제목", example = "게시글 제목")
        String title,
        @Schema(description = "내용", example = "게시글 내용")
        String content,
        @Schema(description = "좋아요 수", example = "10")
        Integer likeCount,
        @Schema(description = "댓글 수", example = "5")
        Integer commentCount,
        @Schema(description = "작성자 닉네임", example = "아트피커")
        String nickname
) {
    public static MyPagePostResponseDto from(Post post) {
        return new MyPagePostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getUser().getNickname()
        );
    }
}
