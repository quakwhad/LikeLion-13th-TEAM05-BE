package com.likelion.artipick.search.api;

import com.likelion.artipick.culture.api.dto.response.CultureListResponse;
import com.likelion.artipick.global.code.dto.ApiResponse;
import com.likelion.artipick.search.application.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "검색", description = "문화행사 검색 API")
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "문화행사 검색", description = "키워드, 지역, 카테고리로 문화행사를 검색합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<CultureListResponse>>> searchCulture(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String category,
            @Parameter(description = "페이지 정보") @PageableDefault(size = 10) Pageable pageable) {

        Page<CultureListResponse> cultures = searchService.searchCultures(keyword, location, category, pageable)
                .map(CultureListResponse::from);

        return ResponseEntity.ok(ApiResponse.onSuccess(cultures));
    }
}
