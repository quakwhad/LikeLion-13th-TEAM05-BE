package com.likelion.artipick.global.security;

import com.likelion.artipick.user.domain.User;
import com.likelion.artipick.user.domain.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String REISSUE_URL = "/auth/reissue";

    private final JwtService jwtService;
    private final UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 토큰 재발급 요청인 경우
        if (request.getRequestURI().equals(REISSUE_URL)) {
            String refreshToken = jwtService.extractRefreshToken(request)
                    .filter(jwtService::isTokenValid)
                    .orElse(null);

            if (refreshToken != null) {
                checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
                return; // 재발급 후 필터 종료
            }
            // 재발급 요청에 유효한 리프레시 토큰이 없으면 그냥 통과 (뒤에서 403 처리됨)
            filterChain.doFilter(request, response);
            return;
        }

        // 2. 일반 API 요청인 경우 (AccessToken 확인)
        checkAccessTokenAndAuthentication(request, response, filterChain);
    }

    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        userRepository.findByRefreshToken(refreshToken)
                .ifPresent(user -> {
                    String reIssuedAccessToken = jwtService.createAccessToken(user.getEmail());
                    jwtService.sendAccessToken(response, reIssuedAccessToken);
                });
    }
    
    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                  FilterChain filterChain) throws ServletException, IOException {
        log.info("checkAccessTokenAndAuthentication() 호출");
        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .flatMap(jwtService::extractEmail)
                .flatMap(userRepository::findByEmail)
                .ifPresent(this::saveAuthentication);

        filterChain.doFilter(request, response);
    }

    public void saveAuthentication(User myUser) {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(myUser, null, authoritiesMapper.mapAuthorities(myUser.getAuthorities()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

