# 구현 작업 원칙
- SOLID 원칙을 사용해서 구현
- REST API 원칙을 준수
- API 문서화: Swagger/OpenAPI를 활용한 자동 문서화를 구현
- 구조: api(dto/request,response), domain(entity,repository), application
- DTO는 Record 사용
- 공통 API 응답 포맷 통일
- 확적성 높은 설계
- RequestDTO에는 example 넣기
- 목록 조회를 할 때는 List가 좋은 경우를 빼고는 Page 처리 (간략한 정보만 표시할 DTO 사용)

# 코드 품질 원칙
- 단순성: 언제나 복잡한 솔루션보다 가장 단순한 솔루션을 사용
- 중복 방지: 코드 중복을 피하고, 가능한 기존 기능을 재사용(DRY 원칙)
- 가드레일: 테스트 이외는 개발이나 프로덕션 환경에서 모의 데이터를 사용 금지
- 효율성: 명확성을 희생하지 않으면서 토큰 사용을 최소화하도록 출력을 최적화
- DTO는 빌더 패턴은 of, from, toEntity를 사용
- 서비스 코드에서 builder를 이용해 많은 정보를 entity로 매핑할 때는 toEntity 사용
- 오류 코드와 성공 코드는 ErrorCode, SuccessCode라는 enum으로 사용
- 적절한 예외처리
- 메서드 길이는 15줄을 넘지 않는다. (SRP)
- else를 사용을 지양
- stream 사용 시 stream 뒤에 줄바꿈
- 의미가 명확한 변수 이름과 메서드 이름 사용
- 엔티티는 빌더를 사용
- 도메인 객체 생성 로직을 Service에서 복잡하게 생성하게 되면 정적 팩토리 메서드나 DTO를 활용
- 중복되는 코드는 헬퍼 메서드로 분리
- JPQL은 꼭 필요한 경우에만 사용

# 메서드 시작명 컨벤션
- 생성 : create
- 수정 : update
- 삭제 : delete
- 조회 : find

# 리팩토링
- 리팩토링이 필요한 경우 계획을 설명하고 허락을 받은 다음 진행
- 코드 구조를 개선하는 것이 목표이며, 기능 변경은 아님

# 배포 및 운영
- 환경 분리: 개발/테스트/운영 환경을 명확히 분리
- 설정 외부화: application.yml로 환경별 설정을 관리

# 보안 원칙
- JWT/OAuth 인증 구현
- 민감 정보는 application-secret.yml에 따로 관리하고 gitignore에 추가
- JPA 사용으로 SQL 인젝션 방지

# 성능 및 확장성
- 페이징: 데이터 목록 조회 시 페이징 처리를 필수

# 로깅
- 로그 레벨: ERROR, WARN, INFO, DEBUG를 적절히 구분
