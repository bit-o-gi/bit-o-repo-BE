package com.bito.be.auth.controller;

import com.bito.be.auth.dto.AccessTokenCreateRequest;
import com.bito.be.auth.dto.AccessTokenResponse;
import com.bito.be.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthControllerDoc {

    private final TokenService tokenService;

    @PostMapping("/token")
    public ResponseEntity<AccessTokenResponse> createAccessToken(@RequestBody AccessTokenCreateRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(new AccessTokenResponse(newAccessToken));
    }

    @PostMapping("/fake/token")
    public ResponseEntity<AccessTokenResponse> createFakeAccessToken() {
        String newAccessToken = tokenService.createNewFakeToken();
        return ResponseEntity.status(HttpStatus.CREATED).body(new AccessTokenResponse(newAccessToken));
    }
}
