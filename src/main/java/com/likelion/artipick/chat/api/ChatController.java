package com.likelion.artipick.chat.api;

import com.likelion.artipick.chat.api.dto.request.ChatRequest;
import com.likelion.artipick.chat.api.dto.response.ChatHistoryResponse;
import com.likelion.artipick.chat.application.ChatService;
import com.likelion.artipick.global.code.dto.ApiResponse;
import com.likelion.artipick.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "채팅 API", description = "AI 챗봇과의 대화")
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "챗봇에게 메시지 전송", description = "사용자 메시지를 AI 챗봇에게 전송합니다.")
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<String>> sendMessage(
            @Valid @RequestBody ChatRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String aiResponse = chatService.sendMessage(request, userDetails.getUserId()).block();
        return ResponseEntity.ok(ApiResponse.onSuccess(aiResponse));
    }

    @Operation(summary = "대화 기록 조회", description = "사용자의 전체 대화 기록을 조회합니다.")
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<ChatHistoryResponse>>> getChatHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ChatHistoryResponse> history = chatService.getChatHistory(userDetails.getUserId()).block();
        return ResponseEntity.ok(ApiResponse.onSuccess(history));
    }
}
