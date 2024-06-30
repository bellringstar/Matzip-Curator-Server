# Matzip-Curator-Sever
### [기획서](./docs/project-proposal.md)
---
## 1. 프로젝트 개요

프로젝트명: 맛집 큐레이터(Matzip Curator)
목적: 사용자 맞춤형 맛집 추천 및 리뷰 플랫폼 개발
주요 기능: 맛집 검색, 리뷰 작성, 개인화 추천, 실시간 알림

## 2. 기술 스택 선정 및 근거

### 2.1 핵심 기술

1. **Java 17 & Spring Boot 3.1**
    - 선택 이유:
        - Java의 안정성과 Spring Boot의 빠른 개발 속도 결합
        - Java 17의 새로운 기능(sealed classes, pattern matching 등)으로 코드 품질 향상
        - Spring Boot 3.1의 향상된 성능과 보안 기능 활용
    - 대안과의 비교:
        - Kotlin: 간결한 문법이 장점이나, Java에 비해 레거시 시스템과의 호환성이 떨어짐
        - Node.js: 비동기 처리에 강점이 있으나, 대규모 엔터프라이즈 애플리케이션에서 Java가 더 안정적이며 성능 예측이 용이함

2. **Spring Data JPA & Hibernate**
    - 선택 이유:
        - 객체-관계 매핑(ORM)을 통한 개발 생산성 향상
        - 데이터베이스 벤더 독립성 확보
        - 복잡한 쿼리를 메소드 이름으로 간단히 표현 가능
    - 대안과의 비교:
        - MyBatis: SQL 직접 작성으로 세밀한 제어가 가능하나, 동적 쿼리 작성 시 JPA보다 복잡함
        - JDBC Template: 로우 레벨 제어가 가능하지만, 반복적인 코드가 많아 생산성이 JPA에 비해 떨어짐

3. **Querydsl**
    - 선택 이유:
        - 타입 안전한 쿼리 작성으로 컴파일 시점에 오류 검출 가능
        - 복잡한 동적 쿼리 처리가 JPQL이나 Criteria API보다 직관적이고 간단함
        - IDE의 자동 완성 기능을 활용할 수 있어 개발 효율성 증가
    - 대안과의 비교:
        - JPQL: 문자열 기반 쿼리로 런타임 시 오류 발견, 복잡한 동적 쿼리 작성이 어려움
        - Criteria API: 타입 안전하지만 API가 복잡하고 가독성이 떨어짐

### 2.2 데이터베이스

1. **MySQL 8.0**
    - 선택 이유:
        - 높은 안정성과 성능, 특히 트랜잭션 처리에 강점
        - 풍부한 레퍼런스와 커뮤니티 지원
        - 무료 사용 가능하며, 대규모 데이터 처리에도 적합
    - 대안과의 비교:
        - PostgreSQL: 고급 기능(예: JSON 지원)이 더 풍부하지만, MySQL이 더 대중적이고 호스팅 서비스 지원이 많음
        - Oracle: 고성능과 많은 기능을 제공하지만, 라이센스 비용이 높고 관리가 복잡함

2. **Redis**
    - 선택 이유:
        - 인메모리 데이터 구조로 초고속 읽기/쓰기 가능
        - 다양한 데이터 구조(String, List, Set, Sorted Set, Hash 등) 지원으로 유연한 캐싱 전략 수립 가능
        - 퍼블리셔/서브스크라이버 모델 지원으로 실시간 알림 기능 구현에 적합
    - 대안과의 비교:
        - Memcached: 단순한 key-value 저장에 특화되어 있지만, Redis가 더 다양한 데이터 타입을 지원하고 영속성 보장이 쉬움
        - Hazelcast: 분산 캐싱에 강점이 있지만, Redis가 더 널리 사용되고 레퍼런스가 풍부함

### 2.3 메시지 큐

**Apache Kafka**
- 선택 이유:
    - 높은 처리량과 낮은 지연시간으로 실시간 데이터 처리에 적합
    - 분산 시스템 구성이 용이하여 높은 확장성과 가용성 제공
    - 메시지의 영속성을 보장하여 데이터 손실 위험 감소
- 대안과의 비교:
    - RabbitMQ: AMQP 프로토콜 지원으로 다양한 메시징 패턴 구현이 가능하나, 대용량 처리에서 Kafka가 더 뛰어난 성능을 보임
    - Apache ActiveMQ: JMS 지원으로 Java 애플리케이션과의 통합이 쉽지만, 대규모 처리와 확장성 면에서 Kafka가 우수함

### 2.4 검색 엔진

**Elasticsearch 7.x**
- 선택 이유:
    - 풀텍스트 검색 기능이 뛰어나 맛집 이름, 메뉴, 리뷰 내용 등의 검색에 최적화
    - 분산 아키텍처로 대용량 데이터 처리와 확장성이 뛰어남
    - 실시간 분석과 집계 기능을 제공하여 추천 시스템 구현에 활용 가능
- 대안과의 비교:
    - Apache Solr: 엔터프라이즈 검색에 강점이 있지만, 실시간 데이터 처리와 분산 처리에서 Elasticsearch가 더 우수한 성능을 보임
    - Algolia: 호스팅 서비스로 초기 설정이 쉽지만, 대규모 데이터 처리 시 비용이 높아지며 커스터마이징의 제한이 있음

## 3. 시스템 아키텍처

### 3.1 전체 아키텍처 개요

```
[Client] <-> [API Gateway] <-> [Authentication Server]
                 |
    +------------+------------+
    |            |            |
[User Service] [Restaurant Service] [Review Service]
    |            |            |
    +------------+------------+
                 |
         [Recommendation Service]
                 |
    +------------+------------+
    |            |            |
 [MySQL]      [Redis]     [Elasticsearch]
```

### 3.2 주요 컴포넌트

1. API Gateway: 클라이언트 요청의 첫 진입점, 라우팅 및 인증 처리
2. Authentication Server: 사용자 인증 및 권한 관리
3. User Service: 사용자 정보 관리 및 프로필 처리
4. Restaurant Service: 맛집 정보 관리 및 검색 기능 제공
5. Review Service: 리뷰 작성, 조회, 평점 관리
6. Recommendation Service: 사용자 맞춤 추천 알고리즘 실행

### 3.3 데이터 흐름

1. 클라이언트 요청 -> API Gateway
2. API Gateway에서 인증 처리 후 해당 서비스로 라우팅
3. 각 서비스는 필요에 따라 데이터베이스(MySQL) 접근
4. 빠른 응답이 필요한 데이터는 Redis에서 캐싱
5. 검색 요청은 Elasticsearch를 통해 처리
6. 이벤트 기반 처리(예: 리뷰 작성 시 추천 갱신)는 Kafka를 통해 비동기로 처리

## 4. 도메인 중심 패키지 구조

```
com.matzipcurator
├── user
│   ├── controller
│   ├── service
│   ├── repository
│   ├── domain
│   └── dto
├── restaurant
│   ├── controller
│   ├── service
│   ├── repository
│   ├── domain
│   └── dto
├── review
│   ├── controller
│   ├── service
│   ├── repository
│   ├── domain
│   └── dto
├── recommendation
│   ├── controller
│   ├── service
│   ├── repository
│   ├── domain
│   └── dto
├── search
│   ├── controller
│   ├── service
│   ├── repository
│   └── dto
└── common
    ├── config
    ├── exception
    ├── security
    └── util
```

이 구조의 장점:
1. 높은 응집도: 관련 기능이 한 패키지에 모여 있어 코드 이해와 유지보수가 용이
2. 낮은 결합도: 도메인 간 의존성을 최소화하여 변경의 영향 범위를 줄임
3. 확장성: 새로운 도메인 추가가 용이하며, 마이크로서비스로의 전환이 수월함

## 5. 데이터 모델링

### 5.1 주요 엔티티 및 관계

1. User
    - id: Long (PK)
    - username: String
    - email: String
    - password: String (해시된 값)
    - createdAt: LocalDateTime
    - updatedAt: LocalDateTime

2. Restaurant
    - id: Long (PK)
    - name: String
    - address: String
    - latitude: Double
    - longitude: Double
    - category: String
    - averageRating: Double
    - createdAt: LocalDateTime
    - updatedAt: LocalDateTime

3. Review
    - id: Long (PK)
    - userId: Long (FK to User)
    - restaurantId: Long (FK to Restaurant)
    - content: String
    - rating: Integer
    - createdAt: LocalDateTime
    - updatedAt: LocalDateTime

4. UserPreference
    - id: Long (PK)
    - userId: Long (FK to User)
    - category: String
    - preferenceScore: Double

### 5.2 관계 설명

- User - Review: 일대다 (한 사용자가 여러 리뷰를 작성)
- Restaurant - Review: 일대다 (한 맛집에 여러 리뷰가 존재)
- User - UserPreference: 일대다 (한 사용자가 여러 카테고리에 대한 선호도를 가짐)

### 6.1 RESTful API 엔드포인트

1. 사용자 관리
    - POST /api/v1/users - 사용자 등록
    - GET /api/v1/users/{id} - 사용자 정보 조회
    - PUT /api/v1/users/{id} - 사용자 정보 수정
    - DELETE /api/v1/users/{id} - 사용자 삭제

2. 인증
    - POST /api/v1/auth/login - 로그인
    - POST /api/v1/auth/logout - 로그아웃
    - POST /api/v1/auth/refresh - 토큰 갱신

3. 맛집
    - POST /api/v1/restaurants - 맛집 등록
    - GET /api/v1/restaurants/{id} - 맛집 정보 조회
    - PUT /api/v1/restaurants/{id} - 맛집 정보 수정
    - DELETE /api/v1/restaurants/{id} - 맛집 삭제
    - GET /api/v1/restaurants - 맛집 목록 조회 (페이징, 필터링)

4. 리뷰
    - POST /api/v1/reviews - 리뷰 작성
    - GET /api/v1/reviews/{id} - 리뷰 조회
    - PUT /api/v1/reviews/{id} - 리뷰 수정
    - DELETE /api/v1/reviews/{id} - 리뷰 삭제
    - GET /api/v1/restaurants/{id}/reviews - 특정 맛집의 리뷰 목록 조회

5. 추천
    - GET /api/v1/recommendations - 사용자 맞춤 맛집 추천
    - GET /api/v1/recommendations/popular - 인기 맛집 추천

6. 검색
    - GET /api/v1/search - 통합 검색 (맛집, 메뉴, 리뷰)

### 6.2 API 버저닝 전략

- URI 경로에 버전 정보 포함 (예: /api/v1/)
- 새로운 버전 출시 시 이전 버전 지원 기간 명시 (예: v1은 v2 출시 후 6개월간 지원)

## 7. 성능 최적화 전략

### 7.1 데이터베이스 최적화

1. 인덱싱
    - 자주 조회되는 컬럼에 대해 인덱스 생성 (예: Restaurant의 name, category)
    - 복합 인덱스 활용 (예: latitude, longitude를 함께 인덱싱하여 위치 기반 검색 최적화)

2. 쿼리 최적화
    - Querydsl을 활용한 동적 쿼리 최적화
    - 불필요한 조인 최소화 및 페이징 처리 적용

3. 데이터베이스 파티셔닝
    - 대용량 데이터(예: 리뷰)에 대해 파티셔닝 적용하여 조회 성능 향상

### 7.2 캐싱 전략

1. 애플리케이션 레벨 캐싱
    - Spring Cache 추상화 활용
    - 자주 조회되는 데이터(예: 인기 맛집 목록)에 대해 로컬 캐시 적용

2. 분산 캐싱 (Redis 활용)
    - 세션 저장소로 활용하여 무상태 서버 구현
    - 실시간 랭킹 정보 저장 및 조회
    - 사용자별 추천 결과 캐싱

### 7.3 비동기 처리

1. @Async 어노테이션 활용
    - 이메일 발송, 로그 기록 등 비동기로 처리 가능한 작업에 적용

2. CompletableFuture 활용
    - 복잡한 비동기 작업의 조합 및 예외 처리에 사용

3. 이벤트 기반 아키텍처
    - Kafka를 활용한 이벤트 스트리밍으로 실시간 데이터 처리 (예: 리뷰 작성 시 평점 업데이트)

## 8. 보안 설계

### 8.1 인증 및 인가

1. JWT 기반 인증
    - Access Token과 Refresh Token 활용
    - Token 저장소로 Redis 사용하여 토큰 무효화 관리

2. OAuth 2.0 소셜 로그인
    - Google, Kakao 등 주요 소셜 플랫폼 연동

3. Spring Security 활용
    - 역할 기반 접근 제어 (RBAC) 구현
    - 보안 필터 체인 구성

### 8.2 데이터 보안

1. 암호화
    - 사용자 비밀번호 bcrypt 알고리즘으로 해시 처리
    - 중요 데이터 AES 암호화 적용

2. HTTPS 적용
    - 모든 API 통신에 SSL/TLS 프로토콜 적용

3. API 요청 제한
    - Redis를 활용한 Rate Limiting 구현

### 8.3 보안 모니터링 및 감사

1. Spring Actuator를 활용한 애플리케이션 상태 모니터링
2. AOP를 활용한 주요 작업 로깅 및 감사 추적
3. 실시간 알림 시스템 구축 (비정상적인 접근 탐지 시)

## 9. 확장성 및 유지보수성

### 9.1 확장성 고려사항

1. 수평적 확장
    - 스테이트리스 설계로 서버 인스턴스 수평 확장 용이
    - 로드 밸런서 활용 (예: AWS ELB)

2. 데이터베이스 확장
    - Read Replica 구성으로 읽기 성능 향상
    - 샤딩 전략 수립 (사용자 ID 기반 샤딩)

3. 캐시 계층 확장
    - Redis Cluster 구성으로 캐시 용량 및 성능 확장

### 9.2 유지보수성 향상 전략

1. 코드 품질 관리
    - SonarQube를 활용한 정적 코드 분석
    - 단위 테스트 및 통합 테스트 자동화 (JUnit, Mockito 활용)

2. 문서화
    - Swagger를 활용한 API 문서 자동화
    - 주요 비즈니스 로직에 대한 상세 주석 작성

3. 모니터링 및 로깅
    - ELK 스택 (Elasticsearch, Logstash, Kibana) 활용한 중앙 집중식 로깅
    - Prometheus와 Grafana를 활용한 실시간 모니터링 대시보드 구축

4. CI/CD 파이프라인
    - GitHub Actions를 활용한 지속적 통합 및 배포
    - 환경별 (개발, 스테이징, 운영) 배포 전략 수립

## 10. 결론

이 백엔드 설계는 확장 가능하고 유지보수가 용이한 구조를 지향합니다. 도메인 중심의 패키지 구조, 효율적인 데이터 처리, 강력한 보안 메커니즘을 통해 안정적이고 성능이 뛰어난 시스템을 구축할 수 있습니다.

최신 기술 스택을 활용함으로써 개발 생산성을 높이고, 실제 서비스 운영 환경과 유사한 경험을 제공할 수 있습니다. 또한, 확장성을 고려한 설계로 향후 서비스 규모가 커지더라도 유연하게 대응할 수 있습니다.

이 프로젝트를 통해 현대적인 백엔드 개발 기술과 아키텍처를 실제로 적용해 볼 수 있으며, 이는 실무에서 요구되는 기술력과 경험을 쌓는 데 큰 도움이 될 것입니다.
