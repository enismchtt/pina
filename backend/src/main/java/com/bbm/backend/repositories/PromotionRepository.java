package com.bbm.backend.repositories;

import com.bbm.backend.models.Promotion;
import com.bbm.backend.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Optional<Promotion> findByCode(String code);
    List<Promotion> findByActive(boolean active);
    List<Promotion> findByRestaurant(Restaurant restaurant);


    //If there is a other way of selection active ones we should use it
    @Query("SELECT p FROM Promotion p WHERE p.active = true AND p.startDate <= ?1 AND p.endDate >= ?1")
    List<Promotion> findActivePromotions(LocalDateTime currentTime);
}