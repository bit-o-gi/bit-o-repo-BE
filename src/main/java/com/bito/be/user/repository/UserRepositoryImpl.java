package com.bito.be.user.repository;

import com.bito.be.user.domain.User;
import com.bito.be.user.entity.UserEntity;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findById(long id) {
        return userJpaRepository.findById(id).map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserEntity::toDomain);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(UserEntity.from(user)).toDomain();
    }

    @Override
    public void deleteAll() {
        userJpaRepository.deleteAll();
    }
}
