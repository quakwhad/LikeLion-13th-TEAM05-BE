package com.likelion.artipick.culture.api;

import com.likelion.artipick.culture.api.dto.request.CultureRequest;
import com.likelion.artipick.culture.api.dto.response.CultureListResponse;
import com.likelion.artipick.culture.api.dto.response.CultureResponse;
import com.likelion.artipick.culture.application.CultureService;
import com.likelion.artipick.global.code.dto.ApiResponse;
import com.likelion.artipick.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "문화행사", description = "문화행사 관련 API")
@RestController
@RequestMapping("/api/cultures")
@RequiredArgsConstructor
public class CultureController {

    private final CultureService cultureService;

    @Operation(summary = "문화행사 목록 조회", description = "문화행사 목록을 페이징하여 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<CultureListResponse>>> getCulture(
            @Parameter(description = "페이지 정보")
            @PageableDefault(size = 20) Pageable pageable) {

        Page<CultureListResponse> cultures = cultureService.findAllCultures(pageable)
                .map(CultureListResponse::from);

        return ResponseEntity.ok(ApiResponse.onSuccess(cultures));
    }

    @Operation(summary = "문화행사 상세 조회", description = "특정 문화행사의 상세 정보를 조회합니다.")
    @GetMapping("/{cultureId}")
    public ResponseEntity<ApiResponse<CultureResponse>> getCulture(
            @Parameter(description = "문화행사 ID", example = "1") @PathVariable Long cultureId) {

        CultureResponse culture = CultureResponse.from(cultureService.findCultureById(cultureId));
        return ResponseEntity.ok(ApiResponse.onSuccess(culture));
    }

    @Operation(summary = "문화행사 생성", description = "새로운 문화행사를 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<CultureResponse>> createCulture(
            @RequestBody CultureRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.onSuccess(
                CultureResponse.from(cultureService.createCulture(request, user))));
    }

    @Operation(summary = "문화행사 수정", description = "기존 문화행사를 수정합니다.")
    @PatchMapping("/{cultureId}")
    public ResponseEntity<ApiResponse<CultureResponse>> updateCulture(
            @Parameter(description = "문화행사 ID", example = "1") @PathVariable Long cultureId,
            @RequestBody CultureRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.onSuccess(
                CultureResponse.from(cultureService.updateCulture(cultureId, request, user))));
    }

    @Operation(summary = "문화행사 삭제", description = "특정 문화행사를 삭제합니다.")
    @DeleteMapping("/{cultureId}")
    public ResponseEntity<ApiResponse<Void>> deleteCulture(
            @Parameter(description = "문화행사 ID", example = "1") @PathVariable Long cultureId,
            @AuthenticationPrincipal User user) {
        cultureService.deleteCulture(cultureId, user);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @Operation(summary = "문화행사 찜하기", description = "특정 문화행사에 대해 찜하기(좋아요) 를 토글합니다.")
    @PostMapping("/likes/{cultureId}")
    public ResponseEntity<ApiResponse<Void>> toggleLike(
            @Parameter(description = "문화행사 ID", example = "1") @PathVariable Long cultureId,
            @AuthenticationPrincipal User user) {
        cultureService.toggleLike(cultureId, user);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @Operation(summary = "문화행사 위치 조회", description = "특정 문화행사의 위도, 경도 위치 정보를 조회합니다.")
    @GetMapping("/location/{cultureId}")
    public ResponseEntity<ApiResponse<Map<String, String>>> getLocation(
            @Parameter(description = "문화행사 ID", example = "1") @PathVariable Long cultureId) {
        return ResponseEntity.ok(ApiResponse.onSuccess(cultureService.getLocation(cultureId)));
    }
}
