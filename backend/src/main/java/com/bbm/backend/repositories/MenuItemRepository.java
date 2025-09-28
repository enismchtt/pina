package com.bbm.backend.repositories;

import com.bbm.backend.models.MenuItem;
import com.bbm.backend.models.Restaurant;
import com.bbm.backend.utils.enums.CuisineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByRestaurant(Restaurant restaurant);
    List<MenuItem> findByRestaurantAndAvailableTrue(Restaurant restaurant);
    List<MenuItem> findByCategory(CuisineType category);
    List<MenuItem> findByNameContainingIgnoreCase(String name);
    List<MenuItem> findByPriceLessThanEqual(double price);
}
