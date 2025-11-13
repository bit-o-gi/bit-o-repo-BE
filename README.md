# Spring Boot OAuth2 + JWT 소셜 로그인 템플릿

카카오 OAuth2 인증과 JWT 토큰 기반 인가를 구현한 Spring Boot 보일러플레이트입니다.

## 주요 기능

- 🔐 카카오 OAuth2 소셜 로그인
- 🎫 JWT Access Token / Refresh Token 발급
- 🔄 Refresh Token을 이용한 Access Token 재발급
- 🍪 HttpOnly Cookie를 이용한 안전한 Refresh Token 저장
- 📝 Swagger UI를 통한 API 문서화
- 🗄️ MySQL 데이터베이스 연동

## 기술 스택

- **Java 17**
- **Spring Boot 3.3.0**
- **Spring Security + OAuth2 Client**
- **JWT (jjwt 0.9.1)**
- **MySQL**
- **JPA / Hibernate**
- **Lombok**
- **Swagger (springdoc-openapi)**

## 빠른 시작

### 1. 사전 요구사항

- Java 17 이상
- MySQL 8.0 이상
- 카카오 개발자 계정 및 애플리케이션 등록

### 2. 데이터베이스 설정

MySQL에 데이터베이스를 생성합니다:

```sql
CREATE DATABASE oauth2_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 설정 파일 구성

`.env.example` 파일을 복사하여 `.env` 파일을 생성하고 실제 값을 입력합니다:

```bash
cp .env.example .env
```

`.env` 파일에서 다음 값들을 수정합니다:

- 데이터베이스 접속 정보 (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`)
- 카카오 OAuth2 인증 정보 (`KAKAO_CLIENT_ID`, `KAKAO_CLIENT_SECRET`)
- 카카오 리다이렉트 URI (`KAKAO_REDIRECT_URI`)
- JWT 설정 (`JWT_ISSUER`, `JWT_SECRET_KEY`)
- 프론트엔드 리다이렉트 URL (`FRONTEND_REDIRECT_URL`)

### 4. 애플리케이션 실행

```bash
./gradlew bootRun
```

애플리케이션이 실행되면 http://localhost:8080 에서 접근 가능합니다.

### 5. Swagger UI

API 문서는 http://localhost:8080/swagger-ui.html 에서 확인할 수 있습니다.

## 인증 흐름

### OAuth2 로그인 과정

```
1. 사용자 -> 카카오 로그인 요청
   GET /oauth2/authorization/kakao

2. 카카오 로그인 페이지로 리다이렉트

3. 사용자가 카카오에서 로그인 및 동의

4. 카카오 -> 백엔드 콜백
   GET /login/oauth2/code/kakao?code=...

5. 백엔드 처리:
   - 사용자 정보 조회/저장
   - Access Token 생성 (유효기간: 1일)
   - Refresh Token 생성 (유효기간: 14일)
   - Refresh Token은 HttpOnly Cookie에 저장
   - Refresh Token은 DB에도 저장

6. 프론트엔드로 리다이렉트
   GET http://localhost:3000/oauth/callback?token={access_token}
```

### 프론트엔드에서 토큰 처리

OAuth2 로그인이 완료되면:

1. **Access Token**: URL 쿼리 파라미터로 전달됨
   - 프론트엔드에서 `localStorage` 또는 `sessionStorage`에 저장
   - API 요청 시 `Authorization: Bearer {token}` 헤더에 포함

2. **Refresh Token**: HttpOnly Cookie에 자동 저장됨
   - JavaScript에서 접근 불가 (XSS 공격 방지)
   - 브라우저가 자동으로 쿠키 전송

### API 요청 예시

```javascript
// Access Token을 헤더에 포함하여 요청
fetch('http://localhost:8080/api/v1/users/me', {
  headers: {
    'Authorization': `Bearer ${accessToken}`
  },
  credentials: 'include' // 쿠키 전송을 위해 필요
})
```

### 토큰 갱신

Access Token이 만료되었을 때:

```javascript
// Refresh Token은 쿠키에서 자동 전송됨
fetch('http://localhost:8080/api/v1/auth/token', {
  method: 'POST',
  credentials: 'include' // 쿠키 전송 필수
})
.then(res => res.json())
.then(data => {
  // 새로운 Access Token을 받아서 저장
  localStorage.setItem('accessToken', data.accessToken);
});
```

## API 엔드포인트

### 인증 관련

| Method | Endpoint | 설명 | 인증 필요 |
|--------|----------|------|----------|
| GET | `/oauth2/authorization/kakao` | 카카오 로그인 시작 | ❌ |
| POST | `/api/v1/auth/token` | Access Token 갱신 | ❌ (쿠키 필요) |

### 사용자 관련

| Method | Endpoint | 설명 | 인증 필요 |
|--------|----------|------|----------|
| GET | `/api/v1/users/me` | 내 정보 조회 | ✅ |

## 프로젝트 구조

```
src/main/java/com/oauth2/starter/
├── OAuth2StarterApplication.java
├── auth/                     # 인증 관련
│   ├── controller/          # 토큰 갱신 API
│   ├── domain/              # RefreshToken, UserPrincipal
│   ├── dto/                 # Request/Response DTO
│   ├── repository/          # RefreshToken 저장소
│   └── service/             # OAuth2, Token 서비스
├── config/
│   ├── jwt/                 # JWT 설정 및 필터
│   ├── oauth/               # OAuth2 설정 및 핸들러
│   └── WebSecurityConfig.java
├── user/                    # 사용자 관련
│   ├── domain/              # User 도메인
│   ├── entity/              # UserEntity (JPA)
│   ├── repository/
│   └── service/
├── base/                    # 공통 엔티티
└── util/                    # 유틸리티 클래스
```

## 보안 고려사항

### Refresh Token 보안

- **HttpOnly Cookie**: JavaScript에서 접근 불가하여 XSS 공격 방지
- **Database 저장**: 서버에서 Refresh Token 검증 가능
- **14일 유효기간**: 적절한 만료 기간 설정

### Access Token 보안

- **짧은 유효기간 (1일)**: 토큰 노출 시 피해 최소화
- **Bearer 토큰**: 표준 Authorization 헤더 사용

### JWT Secret Key

- **최소 256비트**: 충분한 길이의 랜덤 문자열 사용
- **환경 변수 관리**: .env 파일을 통해 관리 (.gitignore 포함)

## 커스터마이징

### 다른 OAuth Provider 추가하기

현재는 카카오만 지원하지만, Google, Naver 등을 추가할 수 있습니다:

1. `.env`와 `application.yml`에 provider 설정 추가
2. `OAuth2UserCustomService.java`에서 provider별 사용자 정보 파싱 로직 추가
3. `OauthPlatformType` enum에 새 provider 추가

### 토큰 유효기간 변경

`OAuth2SuccessHandler.java`에서 설정:

```java
public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
```

## 개발 가이드

### 빌드

```bash
./gradlew build
```

### 테스트

```bash
./gradlew test
```

### 실행

```bash
./gradlew bootRun
```

## 주의사항

- `.env` 파일은 절대 Git에 커밋하지 마세요
- 프로덕션 환경에서는 JWT Secret Key를 반드시 변경하세요
- CORS 설정을 프로덕션 환경에 맞게 조정하세요 (`WebSecurityConfig.java`)

## 참고 자료

- [카카오 로그인 개발 가이드](https://developers.kakao.com/docs/latest/ko/kakaologin/common)
- [Spring Security OAuth2 문서](https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html)
- [JWT 소개](https://jwt.io/introduction)

## 라이선스

이 프로젝트는 자유롭게 사용 가능합니다.
