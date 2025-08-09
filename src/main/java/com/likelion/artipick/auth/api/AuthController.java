package com.likelion.artipick.auth.api;

import com.likelion.artipick.global.security.JwtService;
import com.likelion.artipick.user.domain.User;
import com.likelion.artipick.user.domain.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 API (테스트용 포함)")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Operation(summary = "테스트용 토큰 발급", description = "DB에 존재하는 사용자의 이메일을 파라미터로 넘기면, 해당 사용자의 유효한 Access Token을 발급합니다.")
    @GetMapping("/test-token")
    public ResponseEntity<String> getTestToken(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 사용자를 찾을 수 없습니다: " + email));
        String accessToken = jwtService.createAccessToken(user.getEmail());
        return ResponseEntity.ok("Bearer " + accessToken);
    }
}
