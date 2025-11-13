package com.bito.be.auth.service;

import com.bito.be.auth.domain.RefreshToken;

public interface RefreshTokenService {
    RefreshToken getByRefreshToken(String refreshToken);
}
