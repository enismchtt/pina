package com.bbm.backend.services.abstractions;

import com.bbm.backend.models.Promotion;

import java.util.*;

public interface IPromotionService {
    List<Promotion> findAll();
    Optional<Promotion> findById(Long id);
    Optional<Promotion> findByCode(String code);
    Promotion save(Promotion promotion);
    void deleteById(Long id);
    List<Promotion> findActivePromotions();
    List<Promotion> findByRestaurantId(Long restaurantId);
    double applyPromotion(String code, double amount);
    void activatePromotion(Long promotionId);
    void deactivatePromotion(Long promotionId);
}
