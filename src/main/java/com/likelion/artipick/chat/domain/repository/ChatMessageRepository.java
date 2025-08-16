package com.likelion.artipick.chat.domain.repository;

import com.likelion.artipick.chat.domain.ChatMessage;
import com.likelion.artipick.chat.domain.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 특정 사용자의 최신 AI 응답 조회
    @Query("SELECT c FROM ChatMessage c " +
            "WHERE c.userId = :userId AND c.messageType =:messageType " +
            "ORDER BY c.createdAt DESC LIMIT 1")
    Optional<ChatMessage> findLatestMessageByUserIdAndType(@Param("userId") Long userId, @Param("messageType") MessageType messageType);

    // 특정 사용자의 모든 채팅 기록 조회 (시간순)
    List<ChatMessage> findByUserIdOrderByCreatedAtAsc(Long userId);
}
