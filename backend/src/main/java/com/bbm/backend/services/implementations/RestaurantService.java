package com.bbm.backend.services.implementations;

import com.bbm.backend.exceptions.ResourceNotFoundException;
import com.bbm.backend.models.Courier;
import com.bbm.backend.models.MenuItem;
import com.bbm.backend.models.Restaurant;
import com.bbm.backend.repositories.CourierRepository;
import com.bbm.backend.repositories.MenuItemRepository;
import com.bbm.backend.repositories.RestaurantRepository;
import com.bbm.backend.services.abstractions.IRestaurantService;
import com.bbm.backend.util.PasswordUtil;
import com.bbm.backend.utils.enums.CuisineType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RestaurantService implements IRestaurantService {
    @Autowired
    private final RestaurantRepository restaurantRepository;
    @Autowired
    private final MenuItemRepository menuItemRepository;
    @Autowired
    private final CourierRepository courierRepository;


    @Override
    public List<Restaurant> findByApprovedTrue() {
        return restaurantRepository.findByApprovedTrue();
    }

    @Override
    public List<Restaurant> findByCuisineType(CuisineType cuisineType) {
        return restaurantRepository.findByCuisineType(cuisineType);
    }

    @Override
    public List<Restaurant> findByRatingGreaterThanEqual(double rating) {
        return restaurantRepository.findByRatingGreaterThanEqual(rating);
    }

    @Override
    public List<Restaurant> findByNameContainingIgnoreCase(String name) {
        return restaurantRepository.findByNameContainingIgnoreCase(name);
    }


    @Override
    public List<MenuItem> getMenuItems(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));


        return menuItemRepository.findByRestaurant(restaurant);
    }

    @Override
    public Optional<MenuItem> getMenuItem(Long menuItemId) {
        return menuItemRepository.findById(menuItemId);
    }

    @Override
    @Transactional
    public void addMenuItem(Long restaurantId, MenuItem menuItem) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        menuItem.setRestaurant(restaurant);
        menuItemRepository.save(menuItem);
    }

    @Override
    @Transactional
    public void updateMenuItem(Long restaurantId, MenuItem menuItem) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        if (!menuItem.getRestaurant().getId().equals(restaurant.getId())) {
            throw new IllegalArgumentException("Menu item does not belong to this restaurant");
        }

        menuItemRepository.save(menuItem);

    }

    @Override
    @Transactional
    public void deleteMenuItem(Long restaurantId, Long menuItemId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + menuItemId));

        if (!menuItem.getRestaurant().getId().equals(restaurant.getId())) {
            throw new IllegalArgumentException("Menu item does not belong to this restaurant");
        }

        menuItemRepository.delete(menuItem);
    }

    @Override
    @Transactional
    public void approveRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        restaurant.setApproved(true);
        restaurantRepository.save(restaurant);
    }

    @Override
    @Transactional
    public void rejectRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        restaurant.setApproved(false);
        restaurantRepository.save(restaurant);
    }

    @Override
    @Transactional
    public void registerCourier(Long restaurantId, Long courierId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new ResourceNotFoundException("Courier not found with id: " + courierId));

        restaurant.getRegisteredCouriers().add(courier);
        restaurantRepository.save(restaurant);
    }

    @Override
    @Transactional
    public void unregisterCourier(Long restaurantId, Long courierId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        restaurant.getRegisteredCouriers().removeIf(courier -> courier.getId().equals(courierId));
        restaurantRepository.save(restaurant);
    }

    @Override
    public Set<Courier> getRegisteredCouriers(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        return restaurant.getRegisteredCouriers();
    }

    @Override
    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        return restaurantRepository.findById(id);
    }

    @Override
    public Optional<Restaurant> findByUsername(String username) {
        return restaurantRepository.findByUsername(username);
    }

    @Override
    public Optional<Restaurant> findByEmail(String email) {
        return restaurantRepository.findByEmail(email);
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteById(Long id) {
        restaurantRepository.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return restaurantRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return restaurantRepository.existsByUsername(email);
    }
}
