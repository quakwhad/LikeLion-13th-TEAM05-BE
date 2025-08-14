package com.likelion.artipick.mypage.api.dto;

import com.likelion.artipick.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "마이페이지에서 내가 쓰거나 찜한 게시글 목록 조회 DTO")
@Getter
@Builder
@AllArgsConstructor
public class MyPagePostDto {
    @Schema(description = "게시글 아이디", example = "1")
    private Long id;
    @Schema(description = "제목", example = "게시글 제목")
    private String title;
    @Schema(description = "내용", example = "게시글 내용")
    private String content;
    @Schema(description = "좋아요 수", example = "10")
    private Integer likeCount;
    @Schema(description = "댓글 수", example = "5")
    private Integer commentCount;
    @Schema(description = "작성자 닉네임", example = "아트피커")
    private String nickname;

    public static MyPagePostDto from(Post post) {
        return MyPagePostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .nickname(post.getUser().getNickname())
                .build();
    }
}
