package com.likelion.artipick.mypage.application;

import com.likelion.artipick.like.repository.LikeRepository;
import com.likelion.artipick.mypage.api.dto.MyPageDto;
import com.likelion.artipick.mypage.api.dto.ProfileUpdateDto;
import com.likelion.artipick.post.domain.Post;
import com.likelion.artipick.post.repository.PostRepository;
import com.likelion.artipick.user.domain.User;
import com.likelion.artipick.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    @Transactional(readOnly = true)
    public MyPageDto getMyPage(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Page<Post> posts = postRepository.findByUserId(userId, pageable);
        Page<Post> likedPosts = likeRepository.findLikedPostsByUserId(userId, pageable);
        return MyPageDto.of(user, posts, likedPosts);
    }

    @Transactional
    public void updateProfile(Long userId, ProfileUpdateDto profileUpdateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.updateProfile(profileUpdateDto.getNickname(), profileUpdateDto.getIntroduction());
    }
}
