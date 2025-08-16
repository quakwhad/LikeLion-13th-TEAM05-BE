package com.likelion.artipick.chat.application;

import com.likelion.artipick.chat.api.dto.request.AiRequest;
import com.likelion.artipick.chat.api.dto.request.AiServerRequest;
import com.likelion.artipick.chat.api.dto.request.ChatRequest;
import com.likelion.artipick.chat.api.dto.response.ChatHistoryResponse;
import com.likelion.artipick.chat.domain.ChatMessage;
import com.likelion.artipick.chat.domain.MessageType;
import com.likelion.artipick.chat.domain.repository.ChatMessageRepository;
import com.likelion.artipick.global.code.status.ErrorStatus;
import com.likelion.artipick.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final WebClient webClient;

    @Value("${ai.server.url}")
    private String aiServerUrl;

    public Mono<Void> sendMessage(ChatRequest request, Long userId) {
        return Mono.fromCallable(() -> {
                    // 사용자 메시지 저장
                    ChatMessage userMessage = ChatMessage.builder()
                            .userId(userId)
                            .content(request.message())
                            .messageType(MessageType.USER)
                            .build();
                    return chatMessageRepository.save(userMessage);
                })
                .flatMap(savedMessage -> {
                    // AI 서버로 전송할 DTO 생성
                    AiServerRequest aiRequest = AiServerRequest.of(userId, request.message());

                    // AI 서버로 비동기 전송
                    return webClient.post()
                            .uri(aiServerUrl + "/chat")
                            .bodyValue(aiRequest)
                            .retrieve()
                            .bodyToMono(String.class)
                            .doOnNext(response -> log.info("AI 서버 응답 성공: {}", response))
                            .then(); // 응답 내용은 무시하고 완료 신호만 전달
                })
                .onErrorMap(e -> {
                    log.error("AI 서버 통신 오류", e);
                    return new GeneralException(ErrorStatus.AI_SERVER_ERROR);
                });
    }

    public Mono<Void> receiveMessage(AiRequest request) {
        return Mono.fromCallable(() -> {
                    ChatMessage aiResponse = ChatMessage.builder()
                            .userId(request.userId())
                            .content(request.response())
                            .messageType(MessageType.AI)
                            .build();
                    return chatMessageRepository.save(aiResponse);
                })
                .doOnNext(savedMessage -> log.info("AI 응답 저장 완료: {}", savedMessage.getId()))
                .then()  // 저장 완료 후 Void 반환
                .onErrorMap(e -> {
                    log.error("채팅 메시지 저장 오류", e);
                    return new GeneralException(ErrorStatus.CHAT_SAVE_ERROR);
                });
    }

    public Mono<List<ChatHistoryResponse>> getChatHistory(Long userId) {
        return Mono.fromCallable(() -> {
                    List<ChatMessage> messages = chatMessageRepository.findByUserIdOrderByCreatedAtAsc(userId);

                    return messages.stream()
                            .map(message -> ChatHistoryResponse.of(
                                    message.getContent(),
                                    message.getMessageType(),
                                    message.getCreatedAt()
                            ))
                            .toList();
                })
                .onErrorMap(e -> {
                    log.error("채팅 기록 조회 오류", e);
                    return new GeneralException(ErrorStatus.DATABASE_ERROR);
                });
    }
}
