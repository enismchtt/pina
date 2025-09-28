package com.bbm.backend.services.abstractions;

import com.bbm.backend.models.Customer;
import com.bbm.backend.models.Restaurant;

import java.util.List;
import java.util.Set;

public interface ICustomerService extends IUserService<Customer> {

    List<Customer> findByRatingGreaterThanEqual(double rating);

    void addFavoriteRestaurant(Long customerId, Long restaurantId);
    void removeFavoriteRestaurant(Long customerId, Long restaurantId);
    Set<Restaurant> getFavoriteRestaurants(Long customerId);
}
