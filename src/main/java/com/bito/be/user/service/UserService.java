package com.bito.be.user.service;

import com.bito.be.user.domain.User;
import com.bito.be.user.dto.UserCreateRequest;
import java.util.Optional;

public interface UserService {

    User getById(Long id);

    User getByEmail(String email);

    User createUser(UserCreateRequest userCreateRequest);

    Optional<User> findById(Long userId);

    boolean isRegisteredEmail(String email);
}
