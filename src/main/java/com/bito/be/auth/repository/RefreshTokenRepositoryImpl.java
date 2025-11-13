package com.bito.be.auth.repository;

import com.bito.be.auth.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public Optional<RefreshToken> findByRefreshToken(String refreshToken) {
        return refreshTokenJpaRepository.findByRefreshToken(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByUserId(Long userId) {
        return refreshTokenJpaRepository.findByUserId(userId);
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenJpaRepository.save(refreshToken);
    }
}
