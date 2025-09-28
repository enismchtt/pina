package com.bbm.backend.repositories;

import com.bbm.backend.models.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourierRepository extends JpaRepository<Courier,Long> {
    Optional<Courier> findByUsername(String username);
    Optional<Courier> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    List<Courier> findByAvailableTrue();
    List<Courier> findByApprovedTrue();
    List<Courier> findByRatingGreaterThanEqual(double rating);
}