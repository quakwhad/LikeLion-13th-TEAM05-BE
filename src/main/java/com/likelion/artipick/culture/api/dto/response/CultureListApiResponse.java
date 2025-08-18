package com.likelion.artipick.culture.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "문화행사 리스트 API 응답")
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@NoArgsConstructor
public class CultureListApiResponse {

    @Schema(description = "응답 헤더")
    private Header header;

    @Schema(description = "응답 본문")
    private Body body;

    @Schema(description = "응답 헤더 정보")
    @XmlAccessorType(XmlAccessType.FIELD)
    @Getter
    @NoArgsConstructor
    public static class Header {
        @Schema(description = "결과 코드")
        private String resultCode;

        @Schema(description = "결과 메시지")
        private String resultMsg;
    }

    @Schema(description = "응답 본문 데이터")
    @XmlAccessorType(XmlAccessType.FIELD)
    @Getter
    @NoArgsConstructor
    public static class Body {
        @Schema(description = "문화행사 목록")
        private Items items;

        @Schema(description = "페이지당 행 수")
        @XmlElement(name = "numOfrows")
        private Integer numOfRows;

        @Schema(description = "페이지 번호")
        @XmlElement(name = "PageNo")
        private Integer pageNo;

        @Schema(description = "전체 건수")
        private Integer totalCount;
    }

    @Schema(description = "문화행사 아이템 목록")
    @XmlAccessorType(XmlAccessType.FIELD)
    @Getter
    @NoArgsConstructor
    public static class Items {
        @Schema(description = "문화행사 배열")
        @XmlElement(name = "item")
        private List<CultureListItem> item;
    }

    @Schema(description = "문화행사 리스트 정보")
    @XmlAccessorType(XmlAccessType.FIELD)
    @Getter
    @NoArgsConstructor
    public static class CultureListItem {
        @Schema(description = "서비스 분류", example = "공연/전시")
        private String serviceName;

        @Schema(description = "문화행사 고유번호", example = "319005")
        private String seq;

        @Schema(description = "문화행사 제목", example = "패트릭 블랑: 수직정원")
        private String title;

        @Schema(description = "시작일 (yyyyMMdd)", example = "20180616")
        private String startDate;

        @Schema(description = "종료일 (yyyyMMdd)", example = "20281231")
        private String endDate;

        @Schema(description = "장소명", example = "부산현대미술관")
        private String place;

        @Schema(description = "분야명", example = "전시")
        private String realmName;

        @Schema(description = "지역", example = "부산")
        private String area;

        @Schema(description = "시군구", example = "사하구")
        private String sigungu;

        @Schema(description = "썸네일 이미지")
        private String thumbnail;

        @Schema(description = "경도", example = "128.9427422591895")
        private String gpsX;

        @Schema(description = "위도", example = "35.10921642590141")
        private String gpsY;
    }
}
