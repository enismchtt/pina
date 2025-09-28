package com.bbm.backend.repositories;

import com.bbm.backend.utils.enums.CuisineType;
import com.bbm.backend.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    Optional<Restaurant> findByUsername(String username);
    Optional<Restaurant> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    List<Restaurant> findByApprovedTrue();
    List<Restaurant> findByCuisineType(CuisineType cuisineType);
    List<Restaurant> findByRatingGreaterThanEqual(double rating);
    List<Restaurant> findByNameContainingIgnoreCase(String name);
}
