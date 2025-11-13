# 카카오 OAuth2 설정 가이드

이 문서는 카카오 로그인을 사용하기 위한 카카오 개발자 콘솔 설정 방법을 안내합니다.

## 1. 카카오 개발자 계정 생성

1. [카카오 개발자 콘솔](https://developers.kakao.com/)에 접속
2. 카카오 계정으로 로그인
3. 개발자 등록 (처음 접속 시)

## 2. 애플리케이션 등록

### 2.1 새 애플리케이션 추가

1. 개발자 콘솔 메인 페이지에서 **"애플리케이션 추가하기"** 클릭
2. 앱 이름 입력 (예: "My OAuth2 App")
3. 사업자명 입력 (개인 개발자는 이름 입력)
4. **"저장"** 클릭

### 2.2 앱 키 확인

애플리케이션 생성 후 **"앱 설정" > "요약 정보"**에서 다음 키를 확인:

- **REST API 키**: `application-secret.yml`의 `client-id`에 사용
- **Client Secret**: 아직 생성되지 않았다면 다음 단계에서 생성

## 3. Client Secret 생성

### 3.1 보안 설정

1. 좌측 메뉴에서 **"제품 설정" > "카카오 로그인" > "보안"** 선택
2. **"Client Secret"** 섹션에서 **"코드 생성"** 클릭
3. 생성된 코드를 복사하여 안전한 곳에 보관
4. **"활성화 상태"**를 **"활성화"**로 변경
5. **"저장"** 클릭

⚠️ **중요**: Client Secret은 한 번만 표시되므로 반드시 복사해두세요!

## 4. Redirect URI 설정

### 4.1 카카오 로그인 활성화

1. 좌측 메뉴에서 **"제품 설정" > "카카오 로그인"** 선택
2. **"활성화 설정"**에서 **"ON"**으로 변경

### 4.2 Redirect URI 등록

1. **"Redirect URI"** 섹션에서 **"Redirect URI 등록"** 클릭
2. 다음 URI들을 추가:
   ```
   http://localhost:8080/login/oauth2/code/kakao
   ```

   프로덕션 환경이라면 실제 도메인도 추가:
   ```
   https://yourdomain.com/login/oauth2/code/kakao
   ```

3. **"저장"** 클릭

## 5. 동의 항목 설정

### 5.1 필수 동의 항목 설정

1. 좌측 메뉴에서 **"제품 설정" > "카카오 로그인" > "동의 항목"** 선택
2. 다음 항목들을 설정:

#### 닉네임
- **접근 권한**: 필수 동의
- **이유**: 사용자 식별 및 표시

#### 카카오계정(이메일)
- **접근 권한**: 필수 동의
- **이유**: 사용자 고유 식별자로 사용

3. **"저장"** 클릭

### 5.2 동의 항목 검수 (선택사항)

개인정보 보호법에 따라 일부 항목은 검수가 필요할 수 있습니다:
- 개인 개발/테스트 용도라면 검수 없이 사용 가능
- 실제 서비스 운영 시에는 검수 신청 필요

## 6. 플랫폼 등록 (선택사항)

웹 애플리케이션을 등록하여 추가 설정 가능:

1. 좌측 메뉴에서 **"앱 설정" > "플랫폼"** 선택
2. **"Web 플랫폼 등록"** 클릭
3. **사이트 도메인** 입력:
   ```
   http://localhost:8080
   ```
4. **"저장"** 클릭

## 7. application-secret.yml 설정

이제 확인한 정보를 `src/main/resources/secrets/application-secret.yml` 파일에 입력합니다:

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          kakao:
            client-id: YOUR_REST_API_KEY           # 2.2에서 확인한 REST API 키
            client-secret: YOUR_CLIENT_SECRET      # 3.1에서 생성한 Client Secret
            redirect-uri: http://localhost:8080/login/oauth2/code/kakao
            # ... 나머지 설정은 예시 파일 참고
```

## 8. 테스트

### 8.1 로컬 테스트

1. Spring Boot 애플리케이션 실행:
   ```bash
   ./gradlew bootRun
   ```

2. 브라우저에서 접속:
   ```
   http://localhost:8080/oauth2/authorization/kakao
   ```

3. 카카오 로그인 페이지로 리다이렉트되면 성공!

### 8.2 로그인 테스트

1. 카카오 계정으로 로그인
2. 동의 화면에서 **"동의하고 계속하기"** 클릭
3. 설정한 프론트엔드 URL로 리다이렉트되고, URL에 토큰이 포함되어 있으면 성공!
   ```
   http://localhost:3000/oauth/callback?token=eyJhbGc...
   ```

## 9. 문제 해결

### 일반적인 오류

#### "invalid_client" 오류
- Client ID 또는 Client Secret이 잘못됨
- `application-secret.yml` 파일의 값을 다시 확인

#### "redirect_uri_mismatch" 오류
- Redirect URI가 카카오 개발자 콘솔에 등록되지 않음
- 4.2 단계를 다시 확인하여 정확한 URI 등록

#### "insufficient_scope" 오류
- 필요한 동의 항목이 설정되지 않음
- 5.1 단계에서 닉네임, 이메일을 필수 동의로 설정했는지 확인

### 로그 확인

`application.yml`에 로그 레벨 설정:

```yaml
logging:
  level:
    org.springframework.security: DEBUG
    org.springframework.security.oauth2: DEBUG
```

## 10. 프로덕션 배포 시 체크리스트

- [ ] 실제 도메인의 Redirect URI 등록
- [ ] Client Secret 환경 변수로 관리
- [ ] HTTPS 사용
- [ ] 동의 항목 검수 완료 (필요 시)
- [ ] 비즈니스 채널 등록 (서비스 운영 시)

## 참고 자료

- [카카오 로그인 REST API](https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api)
- [카카오 로그인 개요](https://developers.kakao.com/docs/latest/ko/kakaologin/common)
- [카카오 로그인 동의 항목 가이드](https://developers.kakao.com/docs/latest/ko/kakaologin/prerequisite#consent-item)
