package com.bito.be.user.service;

import com.bito.be.user.domain.User;
import com.bito.be.user.dto.UserCreateRequest;
import com.bito.be.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public User createUser(UserCreateRequest userCreateRequest) {
        return userRepository.save(User.from(userCreateRequest));
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public boolean isRegisteredEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
