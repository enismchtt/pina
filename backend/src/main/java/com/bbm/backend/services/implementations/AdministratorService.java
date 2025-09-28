package com.bbm.backend.services.implementations;

import com.bbm.backend.models.Administrator;
import com.bbm.backend.services.abstractions.IAdministratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdministratorService implements IAdministratorService {
    @Override
    public List<Administrator> findAll() {
        return null;
    }

    @Override
    public Optional<Administrator> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Administrator> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<Administrator> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Administrator save(Administrator user) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public boolean existsByUsername(String username) {
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }
}
