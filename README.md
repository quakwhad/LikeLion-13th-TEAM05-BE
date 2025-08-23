# LikeLion-13th-Assignment-Template
🦁 SKHU 멋쟁이사자처럼 13기 해커톤 5팀 (커밋하면칼퇴) 백엔드 레포지토리입니다.

## 🛠️ 기술 스택

### Backend
- **Java 17**
- **Spring Boot 3.5.4**
- **Spring Security** (OAuth 2.0, JWT)
- **Spring Data JPA**
- **MySQL**
- **Redis**

### AI & External APIs
- **WebFlux** (비동기 외부 API 통신)
- **AI 서버 연동** (추천 및 챗봇)
- **공공 문화데이터 API**
- **카카오맵 API**
- **날씨 API**

## 🏗️ 프로젝트 구조

```
src/main/java/com/likelion/artipick/
  ├── chat/                  # AI 챗봇
  ├── culture/               # 문화행사 관리
  ├── global/                # 전역 설정 (Security, OAuth, Redis, S3 등)
  ├── interestcategory/      # 관심 카테고리
  ├── kakao_map/             # 지도 서비스
  ├── mypage/                # 마이페이지
  ├── place/                 # 장소 북마크
  ├── recommendation/        # AI 추천 시스템
  ├── review/                # 리뷰 시스템
  ├── search/                # 검색 기능
  ├── user/                  # 사용자 관리
  ├── weather/               # 날씨 정보
  └── ArtipickApplication.java  # 메인 애플리케이션
```

## 🔧 아키텍처 패턴

### 레이어드 아키텍처 (Layered Architecture)
api (Controller) → application (Service) → domain (Entity, Repository)

### 도메인별 패키지 구조
- **api**: REST API 컨트롤러 및 DTO
- **application**: 비즈니스 로직 서비스
- **domain**: 엔티티, 레포지토리, 도메인 로직

## 🚀 주요 기능

### 1. AI 기반 추천 시스템
- **오늘의 추천**: 매일 2개 문화행사 (관심 카테고리 기반)
- **이달의 추천**: 매월 3개 문화행사
- **Redis 캐싱**: 빠른 응답을 위한 사전 생성 데이터
- **스케줄링**: 자동 데이터 갱신 (매일 6시, 매월 1일)

### 2. AI 챗봇
- 자연어 처리 기반 문화행사 정보 제공
- 키워드 추출 및 맞춤 필터링
- 대화 히스토리 관리

### 3. 문화행사 관리
- 공공 문화데이터 API 연동
- 카테고리별 검색 및 필터링
- 조회수, 좋아요 기반 인기도 측정

### 4. 사용자 기능
- OAuth 2.0 소셜 로그인
- 관심 카테고리 설정
- 마이페이지 관리

### 5. 리뷰 시스템
- 문화행사 후기 작성
- 평점 시스템

## 📱 API 문서

- Swagger UI를 통한 API 문서 제공
  - http://localhost:8080/swagger-ui/index.html

## 🔄 Git 브랜치 전략 및 커밋 컨벤션

 ### 작업 흐름 (PR 규칙)
1. **각자 브랜치에서 작업** 후, `main` 브랜치로 **pull request**
2. 1명 이상의 BE 개발자가 **코드리뷰 완료시** `main` 브랜치로 **머지 가능**

### 커밋 메시지 컨벤션
- `Feat(/#이슈번호)`: 새로운 기능 추가
- `Fix(/#이슈번호)`: 버그 수정
- `Chore(/#이슈번호)`: 빌드 작업
- `Refactor(/#이슈번호)`: 코드 리팩토링 (기능 변경 없음)
- `Docs(/#이슈번호)`: 문서 수정

### 메서드 시작명 컨벤션
- **생성**: `create`
- **수정**: `update`
- **삭제**: `delete`
- **조회**: `find`

## 📋 코드 컨벤션

### 구현 원칙
- **SOLID 원칙** 준수
- **REST API 원칙** 준수
- **DTO는 Record** 사용
- **공통 API 응답 포맷** 통일
- **확장성 높은 설계**

### 코딩 스타일
- 클래스 선언부 아래 **필드**가 오면 한 칸 띄우고 작성
- **메서드 길이**는 15줄을 넘지 않음 (SRP)
- 블록 띄어쓰기는 4칸, LF 사용
- **블록 들여쓰기**는 1단계로 제한
- `else` 사용 지양
- **stream** 사용 시 `stream` 뒤에 줄바꿈

### 데이터 처리
- 목록 조회 시 **페이징 처리** 필수
- RequestBody 사용 시 **@Valid** 사용
- DTO 빌더 패턴: `of`, `from`, `toEntity` 사용


### 필수 환경
- Java 17
- MySQL 8.0+
- Redis 6.0+  
