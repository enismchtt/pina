package com.bbm.backend.services.abstractions;

import com.bbm.backend.models.Courier;
import com.bbm.backend.models.MenuItem;
import com.bbm.backend.models.Restaurant;
import com.bbm.backend.utils.enums.CuisineType;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IRestaurantService extends IUserService<Restaurant> {
    List<Restaurant> findByApprovedTrue();
    List<Restaurant> findByCuisineType(CuisineType cuisineType);
    List<Restaurant> findByRatingGreaterThanEqual(double rating);
    List<Restaurant> findByNameContainingIgnoreCase(String name);
    List<MenuItem> getMenuItems(Long restaurantId);
    Optional<MenuItem> getMenuItem(Long menuItemId);
    void addMenuItem(Long restaurantId, MenuItem menuItem);
    void updateMenuItem(Long restaurantId, MenuItem menuItem);
    void deleteMenuItem(Long restaurantId, Long menuItemId);
    void approveRestaurant(Long restaurantId);
    void rejectRestaurant(Long restaurantId);
    void registerCourier(Long restaurantId, Long courierId);
    void unregisterCourier(Long restaurantId, Long courierId);
    Set<Courier> getRegisteredCouriers(Long restaurantId);
}
