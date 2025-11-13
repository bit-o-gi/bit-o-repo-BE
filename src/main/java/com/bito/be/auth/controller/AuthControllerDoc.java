package com.bito.be.auth.controller;

import com.bito.be.auth.dto.AccessTokenCreateRequest;
import com.bito.be.auth.dto.AccessTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth API", description = "인증/인가 및 로그인 관련 API")
public interface AuthControllerDoc {
    @Operation(summary = "엑세스 토큰 발급 API", description = "리프레시 토큰을 보내 액세스 토큰을 발급한다."
            , responses = {
            @ApiResponse(responseCode = "201", description = "발급 성공")
    })
    ResponseEntity<AccessTokenResponse> createAccessToken(@RequestBody AccessTokenCreateRequest request);

    @Operation(summary = "엑세스 토큰 발급 API (개발자용)", description = "테스트 하기 쉽게 액세스 토큰 발급 (백엔드 개발자용)"
            , responses = {
            @ApiResponse(responseCode = "201", description = "발급 성공")
    })
    ResponseEntity<AccessTokenResponse> createFakeAccessToken();
}
