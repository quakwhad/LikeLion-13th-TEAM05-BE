package com.likelion.artipick.chat.application;

import com.likelion.artipick.chat.api.dto.request.AiServerRequest;
import com.likelion.artipick.chat.api.dto.request.ChatRequest;
import com.likelion.artipick.chat.api.dto.response.ChatHistoryResponse;
import com.likelion.artipick.chat.domain.ChatMessage;
import com.likelion.artipick.chat.domain.MessageType;
import com.likelion.artipick.chat.domain.repository.ChatMessageRepository;
import com.likelion.artipick.culture.domain.Culture;
import com.likelion.artipick.culture.domain.repository.CultureRepository;
import com.likelion.artipick.global.code.status.ErrorStatus;
import com.likelion.artipick.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final CultureRepository cultureRepository;
    private final WebClient webClient;

    @Value("${ai.server.url}")
    private String aiServerUrl;

    public Mono<String> sendMessage(ChatRequest request, Long userId) {
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
                    // 현재 진행 중인 문화행사 조회 (상위 50개만)
                    return Mono.fromCallable(() -> {
                        LocalDate today = LocalDate.now();
                        List<Culture> allCultures = cultureRepository.findByEndDateGreaterThanEqualOrderByViewCountDescLikeCountDesc(today);
                        
                        // AI 서버 부하 방지를 위해 인기 상위 50개만 전송
                        return allCultures.stream()
                                .limit(50)
                                .toList();
                    });
                })
                .flatMap(cultures -> {
                    // AI 서버에 메시지 + 문화행사 데이터 전송
                    AiServerRequest aiRequest = AiServerRequest.of(userId, request.message(), cultures);

                    return webClient.post()
                            .uri(aiServerUrl + "/ask")
                            .header("Content-Type", "application/json")
                            .bodyValue(aiRequest)
                            .retrieve()
                            .bodyToMono(String.class)  // AI가 자연어로 응답
                            .flatMap(aiResponse -> {
                                // AI 응답 저장
                                return Mono.fromCallable(() -> {
                                    ChatMessage aiMessage = ChatMessage.builder()
                                            .userId(userId)
                                            .content(aiResponse)
                                            .messageType(MessageType.AI)
                                            .build();
                                    chatMessageRepository.save(aiMessage);
                                    return aiResponse;  // 자연어 응답 반환
                                });
                            });
                })
                .onErrorMap(e -> {
                    log.error("채팅 처리 오류", e);
                    return new GeneralException(ErrorStatus.AI_SERVER_ERROR);
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
