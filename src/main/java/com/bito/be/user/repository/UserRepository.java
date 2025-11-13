package com.bito.be.user.repository;

import com.bito.be.user.domain.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);

    User save(User user);

    void deleteAll();

}
