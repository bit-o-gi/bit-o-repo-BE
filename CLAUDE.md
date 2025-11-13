# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 프로젝트 개요

카카오 인증을 위한 Spring Boot 3 OAuth2 + JWT 소셜 로그인 보일러플레이트입니다. OAuth2를 인증에 사용하고 JWT 토큰(Access + Refresh)을 인가에 사용합니다.

## 빌드 & 실행 명령어

### 빌드
```bash
./gradlew build
```

### 애플리케이션 실행
```bash
./gradlew bootRun
```

### 테스트 실행
```bash
./gradlew test
```

### 단일 테스트 실행
```bash
./gradlew test --tests com.bito.be.YourTestClass
```

## 설정

애플리케이션 실행 전 필수 설정:

1. `.env` 파일 설정:
   - `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`: MySQL 데이터베이스 연결 정보
   - `KAKAO_CLIENT_ID`, `KAKAO_CLIENT_SECRET`: 카카오 OAuth2 인증 정보
   - `KAKAO_REDIRECT_URI`: 카카오 OAuth2 리다이렉트 URI (기본: http://localhost:8080/login/oauth2/code/kakao)
   - `JWT_ISSUER`, `JWT_SECRET_KEY`: JWT 설정 (secret key는 최소 256비트 랜덤 문자열)
   - `FRONTEND_REDIRECT_URL`: 프론트엔드 리다이렉트 URL (기본: http://localhost:3000/oauth/callback)

2. MySQL 데이터베이스 생성:
   ```sql
   CREATE DATABASE oauth2_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

카카오 개발자 콘솔 상세 설정은 `SETUP.md`를 참고하세요.

## 아키텍처

### 패키지 구조

프로젝트의 루트 패키지는 `com.bito.be`이며, 다음과 같이 구성되어 있습니다:

```
com.bito.be/
├── auth/              # 인증 관련 모듈
│   ├── controller/    # 토큰 관련 API 엔드포인트
│   ├── domain/        # RefreshToken, UserPrincipal 도메인 객체
│   ├── dto/           # Request/Response DTO
│   ├── repository/    # RefreshToken 및 OAuth2 저장소
│   └── service/       # OAuth2, Token 서비스
├── user/              # 사용자 관련 모듈
│   ├── domain/        # User 도메인 객체
│   ├── entity/        # UserEntity (JPA)
│   ├── enums/         # OauthPlatformType 등
│   ├── repository/    # User 저장소
│   └── service/       # User 서비스
├── config/            # 설정 클래스
│   ├── jwt/           # JWT 관련 설정 (TokenProvider, Filter, Properties)
│   ├── oauth/         # OAuth2 관련 설정 (SuccessHandler, Properties)
│   └── WebSecurityConfig.java
├── base/              # 공통 엔티티 (BaseEntity)
└── util/              # 유틸리티 클래스 (CookieUtil)
```

### 계층형 아키텍처

도메인 주도 계층형 아키텍처를 따르며 명확한 관심사 분리를 합니다:

- **Domain Layer** (`domain/`): 순수 비즈니스 객체 (예: `User`, `RefreshToken`)
- **Entity Layer** (`entity/`): 데이터베이스 테이블과 매핑되는 JPA 엔티티
- **Repository Layer** (`repository/`): 인터페이스 + 구현체 패턴의 데이터 접근 계층
- **Service Layer** (`service/`): 인터페이스 + 구현체 패턴의 비즈니스 로직 계층
- **Controller Layer** (`controller/`): REST API 엔드포인트

### Repository 패턴

인터페이스 기반 레포지토리를 명시적 구현체와 함께 사용:
- JPA 레포지토리 (예: `UserJpaRepository`)는 `JpaRepository`를 확장
- 비즈니스 레포지토리 (예: `UserRepository`)는 도메인 작업을 정의
- 구현체 (예: `UserRepositoryImpl`)는 JPA와 도메인 계층을 연결
- 엔티티는 `toDomain()` 및 `from()` 메서드를 통해 도메인 객체와 상호 변환

### 인증 흐름

1. **OAuth2 로그인** (`GET /oauth2/authorization/kakao`):
   - 사용자가 카카오 로그인으로 리다이렉트
   - 카카오 콜백이 `/login/oauth2/code/kakao`로 전달됨
   - `OAuth2UserCustomService`가 사용자 정보를 처리하고 저장/업데이트
   - `OAuth2SuccessHandler`가 토큰 생성:
     - Access Token (1일 유효) - URL 쿼리 파라미터로 전달
     - Refresh Token (14일 유효) - HttpOnly 쿠키 + DB에 저장
   - `spring.app.redirect.path`로 설정된 프론트엔드 URL로 리다이렉트

2. **보호된 API 요청**:
   - `TokenAuthenticationFilter`가 요청을 가로챔
   - `Authorization: Bearer {token}` 헤더에서 Access Token 검증
   - `TokenProvider`를 통해 사용자 인증 정보 로드

3. **토큰 갱신** (`POST /api/v1/auth/token`):
   - HttpOnly 쿠키 또는 Request Body에서 Refresh Token 읽기
   - 데이터베이스와 비교하여 검증
   - 새로운 Access Token 발급

4. **개발용 토큰 생성** (`POST /api/v1/auth/fake/token`):
   - 테스트/개발 목적의 임시 토큰 생성 엔드포인트
   - 프로덕션 환경에서는 제거 권장

### Security 설정

`WebSecurityConfig`에서 설정:
- Stateless 세션 관리 (JWT 기반)
- CORS 모든 origin 허용 (프로덕션 환경에서는 조정 필요)
- 공개 엔드포인트: `/api/v1/auth/token`, `/api/v1/auth/fake/token`, `/oauth2/authorization/**`, Swagger UI
- 보호된 엔드포인트: `/api/v1/**` (인증 필요)
- `/api/**`에 대해 401을 반환하는 커스텀 인증 진입점

### JWT 구현

- **TokenProvider**: jjwt 라이브러리를 사용하여 JWT 토큰 생성 및 검증
- **JwtProperties**: application.yml에서 JWT 설정 로드 (`jwt.issuer`, `jwt.secret-key`)
- Claims 포함 항목: 사용자 ID, 이메일 (subject로), 발급자, 만료 시간
- 서명 알고리즘: HS256

### 주요 설정 클래스

- `WebSecurityConfig`: 메인 보안 설정, 필터 체인 구성
- `OAuth2SuccessHandler`: 인증 후 토큰 생성 및 리다이렉트 처리
- `TokenAuthenticationFilter`: JWT 검증을 위한 사전 인증 필터
- `OAuth2UserCustomService`: 카카오용 커스텀 OAuth2 사용자 로더
- `KakaoOAuth2Properties`: 카카오 OAuth2 설정을 바인딩하는 properties 클래스
- `JwtProperties`: JWT 설정을 바인딩하는 properties 클래스

### JPA Auditing

`BitORepoBE` 메인 클래스에 `@EnableJpaAuditing`이 활성화되어 있으며, `BaseEntity`를 통해 생성/수정 시간을 자동 관리합니다.

## API 엔드포인트

### 인증
- `GET /oauth2/authorization/kakao` - 카카오 로그인 시작
- `POST /api/v1/auth/token` - Access Token 갱신 (refresh_token 필요)
- `POST /api/v1/auth/fake/token` - 개발용 임시 토큰 생성

### 문서화
- `GET /swagger-ui.html` - Swagger UI
- `GET /api-docs` - OpenAPI JSON 스펙

## 토큰 설정

토큰 만료 시간을 수정하려면 `OAuth2SuccessHandler.java`를 편집:
```java
public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
```

## 데이터베이스 스키마

- **oauth2_user**: 사용자 정보 저장 (email, nickname, platform, providerId, connectedDt)
- **refresh_token**: 사용자 ID와 매핑된 refresh token 저장

JPA가 `spring.jpa.hibernate.ddl-auto=update`를 통해 스키마를 자동 생성합니다.

## 새로운 OAuth Provider 추가

Google, Naver 등을 추가하려면:

1. Spring OAuth2 클라이언트 형식에 따라 `application.yml`에 provider 설정 추가
2. 환경 변수를 `.env`에 추가
3. `OAuth2UserCustomService.saveOrUpdate()`에서 provider별 사용자 속성 파싱 로직 업데이트
4. `OauthPlatformType`에 새 enum 값 추가
5. provider별 로직이 필요한 경우 success handler 설정