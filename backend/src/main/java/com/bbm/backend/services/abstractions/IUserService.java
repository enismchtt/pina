package com.bbm.backend.services.abstractions;

import com.bbm.backend.models.User;

import java.util.List;
import java.util.Optional;

public interface IUserService<T extends User> {
    List<T> findAll();
    Optional<T> findById(Long id);
    Optional<T> findByUsername(String username);
    Optional<T> findByEmail(String email);
    T save(T user);
    void deleteById(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
