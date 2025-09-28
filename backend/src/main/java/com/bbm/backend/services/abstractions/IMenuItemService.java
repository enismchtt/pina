package com.bbm.backend.services.abstractions;

import com.bbm.backend.models.MenuItem;
import com.bbm.backend.utils.enums.CuisineType;

import java.util.List;
import java.util.Optional;

public interface IMenuItemService{
    List<MenuItem> findAll();
    Optional<MenuItem> findById(Long id);
    MenuItem save(MenuItem menuItem);
    void deleteById(Long id);
    List<MenuItem> findByRestaurantId(Long restaurantId);
    List<MenuItem> findByRestaurantIdAndAvailableTrue(Long restaurantId);
    List<MenuItem> findByCategory(CuisineType category);
    List<MenuItem> findByNameContaining(String name);
    List<MenuItem> findByPriceLessThanEqual(double price);
    void updateAvailability(Long menuItemId, boolean available);
}