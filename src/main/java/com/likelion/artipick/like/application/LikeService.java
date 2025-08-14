package com.likelion.artipick.like.application;

import com.likelion.artipick.global.code.status.ErrorStatus;
import com.likelion.artipick.global.exception.GeneralException;
import com.likelion.artipick.like.domain.Like;
import com.likelion.artipick.like.api.dto.response.LikeResponseDto;
import com.likelion.artipick.like.domain.repository.LikeRepository;
import com.likelion.artipick.post.domain.Post;
import com.likelion.artipick.post.domain.repository.PostRepository;
import com.likelion.artipick.user.domain.User;
import com.likelion.artipick.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public LikeResponseDto toggleLike(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Optional<Like> likeOpt = likeRepository.findByUserAndPost(user, post);

        if (likeOpt.isPresent()) {
            likeRepository.delete(likeOpt.get());
            return new LikeResponseDto(false, "게시글 찜하기를 취소했습니다.");
        } else {
            Like newLike = Like.builder()
                    .user(user)
                    .post(post)
                    .build();
            likeRepository.save(newLike);
            return new LikeResponseDto(true, "게시글을 찜했습니다.");
        }
    }
}
