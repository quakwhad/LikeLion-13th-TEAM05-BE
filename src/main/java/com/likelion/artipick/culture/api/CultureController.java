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

    @Operation(summary = "문화행사 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<CultureListResponse>>> getCulture(
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20) Pageable pageable) {

        Page<CultureListResponse> cultures = cultureService.findAllCultures(pageable)
                .map(CultureListResponse::from);

        return ResponseEntity.ok(ApiResponse.onSuccess(cultures));
    }

    @Operation(summary = "문화행사 상세 조회")
    @GetMapping("/{cultureId}")
    public ResponseEntity<ApiResponse<CultureResponse>> getCulture(
            @Parameter(description = "문화행사 ID", example = "1") @PathVariable Long cultureId) {

        CultureResponse culture = CultureResponse.from(cultureService.findCultureById(cultureId));
        return ResponseEntity.ok(ApiResponse.onSuccess(culture));
    }

    // CRUD/찜/위치 API

    @Operation(summary = "문화행사 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<CultureResponse>> createCulture(
            @RequestBody CultureRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.onSuccess(
                CultureResponse.from(cultureService.createCulture(request, user))));
    }

    @Operation(summary = "문화행사 수정")
    @PatchMapping("/{cultureId}")
    public ResponseEntity<ApiResponse<CultureResponse>> updateCulture(
            @PathVariable Long cultureId,
            @RequestBody CultureRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.onSuccess(
                CultureResponse.from(cultureService.updateCulture(cultureId, request, user))));
    }

    @Operation(summary = "문화행사 삭제")
    @DeleteMapping("/{cultureId}")
    public ResponseEntity<ApiResponse<Void>> deleteCulture(
            @PathVariable Long cultureId,
            @AuthenticationPrincipal User user) {
        cultureService.deleteCulture(cultureId, user);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @Operation(summary = "문화행사 찜하기")
    @PostMapping("/likes/{cultureId}")
    public ResponseEntity<ApiResponse<Void>> toggleLike(
            @PathVariable Long cultureId,
            @AuthenticationPrincipal User user) {
        cultureService.toggleLike(cultureId, user);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @Operation(summary = "문화행사 위치 조회")
    @GetMapping("/location/{cultureId}")
    public ResponseEntity<ApiResponse<Map<String, String>>> getLocation(
            @PathVariable Long cultureId) {
        var culture = cultureService.findCultureById(cultureId);
        Map<String, String> location = Map.of(
                "gpsX", culture.getGpsX(),
                "gpsY", culture.getGpsY()
        );
        return ResponseEntity.ok(ApiResponse.onSuccess(location));
    }
}
