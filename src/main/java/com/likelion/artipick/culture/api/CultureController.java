package com.likelion.artipick.culture.api;

import com.likelion.artipick.culture.api.dto.response.CultureListResponse;
import com.likelion.artipick.culture.api.dto.response.CultureResponse;
import com.likelion.artipick.culture.application.CultureService;
import com.likelion.artipick.global.code.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "문화행사", description = "문화행사 관련 API")
@RestController
@RequestMapping("/api/cultures")
@RequiredArgsConstructor
public class CultureController {

    private final CultureService cultureService;

    @Operation(summary = "문화행사 목록 조회", description = "문화행사 목록을 페이징하여 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<CultureListResponse>>> getCulture(
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20)Pageable pageable) {

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
}
