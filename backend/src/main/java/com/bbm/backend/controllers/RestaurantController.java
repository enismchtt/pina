package com.bbm.backend.controllers;

import com.bbm.backend.dto.*;
import com.bbm.backend.models.*;
import com.bbm.backend.services.implementations.RestaurantService;
import com.bbm.backend.util.PasswordUtil;
import com.bbm.backend.utils.enums.CuisineType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin(origins = "http://localhost:3000") // Add this line
@RequiredArgsConstructor
public class RestaurantController {

    @Autowired
    private final RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
        List<RestaurantDTO> restaurants = restaurantService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/approved")
    public ResponseEntity<List<RestaurantDTO>> getApprovedRestaurants() {
        List<RestaurantDTO> restaurants = restaurantService.findByApprovedTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/cuisine/{cuisineType}")
    public ResponseEntity<List<RestaurantDTO>> getRestaurantsByCuisine(@PathVariable CuisineType cuisineType) {
        List<RestaurantDTO> restaurants = restaurantService.findByCuisineType(cuisineType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RestaurantDTO>> searchRestaurants(@RequestParam String name) {
        List<RestaurantDTO> restaurants = restaurantService.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable Long id) {
        return restaurantService.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RestaurantDTO> createRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        if (restaurantService.existsByUsername(restaurantDTO.getUsername())) {
            return ResponseEntity.badRequest().build();
        }
        restaurantDTO.setType("restaurant");
        Restaurant restaurant = convertToEntity(restaurantDTO);
        restaurant.setPassword(PasswordUtil.encode(restaurant.getPassword()));
        Restaurant savedRestaurant = restaurantService.save(restaurant);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedRestaurant));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO> updateRestaurant(@PathVariable Long id, @RequestBody RestaurantDTO restaurantDTO) {
        if (restaurantService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Restaurant restaurant = convertToEntity(restaurantDTO);
        restaurant.setId(id);
        restaurant.setPassword(PasswordUtil.encode(restaurant.getPassword()));
        Restaurant updatedRestaurant = restaurantService.save(restaurant);
        return ResponseEntity.ok(convertToDTO(updatedRestaurant));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        if (restaurantService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        restaurantService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Void> approveRestaurant(@PathVariable Long id) {
        if (restaurantService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        restaurantService.approveRestaurant(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> rejectRestaurant(@PathVariable Long id) {
        if (restaurantService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        restaurantService.rejectRestaurant(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<List<MenuItemDTO>> getRestaurantMenu(@PathVariable Long id) {
        if (restaurantService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<MenuItemDTO> menuItems = restaurantService.getMenuItems(id).stream()
                .map(this::convertToMenuItemDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(menuItems);
    }

    @PostMapping("/{id}/menu")
    public ResponseEntity<Void> addMenuItem(@PathVariable Long id, @RequestBody MenuItemDTO menuItemDTO) {
        Optional<Restaurant> optionalRestaurant = restaurantService.findById(id);
        if (optionalRestaurant.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        MenuItem menuItem = convertToMenuItemEntity(menuItemDTO);
//        menuItem.setRestaurant(optionalRestaurant.get());
        restaurantService.addMenuItem(id, menuItem);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}/menu/{itemId}")
    public ResponseEntity<Void> updateMenuItem(@PathVariable Long id, @PathVariable Long itemId, @RequestBody MenuItemDTO menuItemDTO) {
        if (restaurantService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        MenuItem menuItem = convertToMenuItemEntity(menuItemDTO);
        menuItem.setId(itemId);
        restaurantService.updateMenuItem(id, menuItem);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/menu/{itemId}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id, @PathVariable Long itemId) {
        if (restaurantService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        restaurantService.deleteMenuItem(id, itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/couriers")
    public ResponseEntity<Set<CourierDTO>> getRegisteredCouriers(@PathVariable Long id) {
        if (restaurantService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Set<CourierDTO> couriers = restaurantService.getRegisteredCouriers(id).stream()
                .map(this::convertToCourierDTO)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(couriers);
    }

    @PostMapping("/{id}/couriers/{courierId}")
    public ResponseEntity<Void> registerCourier(@PathVariable Long id, @PathVariable Long courierId) {
        if (restaurantService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        restaurantService.registerCourier(id, courierId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/couriers/{courierId}")
    public ResponseEntity<Void> unregisterCourier(@PathVariable Long id, @PathVariable Long courierId) {
        if (restaurantService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        restaurantService.unregisterCourier(id, courierId);
        return ResponseEntity.noContent().build();
    }

    private RestaurantDTO convertToDTO(Restaurant restaurant) {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setId(restaurant.getId());
        dto.setUsername(restaurant.getUsername());
        dto.setPassword(restaurant.getPassword());
        dto.setEmail(restaurant.getEmail());
        dto.setPhoneNumber(restaurant.getPhoneNumber());
        dto.setImageUrl(restaurant.getImageUrl());
        dto.setActive(restaurant.isActive());
        dto.setName(restaurant.getName());
        dto.setDescription(restaurant.getDescription());
        dto.setAddress(restaurant.getAddress());
        dto.setCuisineType(restaurant.getCuisineType());
        dto.setRating(restaurant.getRating());
        dto.setBusinessHours(restaurant.getBusinessHours());
        dto.setApproved(restaurant.isApproved());
        dto.setDeliveryRange(restaurant.getDeliveryRange());
        dto.setType(restaurant.getType());
        dto.setBanned(restaurant.isBanned());
        return dto;
    }

    private Restaurant convertToEntity(RestaurantDTO dto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(dto.getId());
        restaurant.setUsername(dto.getUsername());
        restaurant.setPassword(dto.getPassword());
        restaurant.setEmail(dto.getEmail());
        restaurant.setPhoneNumber(dto.getPhoneNumber());
        restaurant.setImageUrl(dto.getImageUrl());
        restaurant.setActive(dto.isActive());
        restaurant.setName(dto.getName());
        restaurant.setDescription(dto.getDescription());
        restaurant.setAddress(dto.getAddress());
        restaurant.setCuisineType(dto.getCuisineType());
        restaurant.setRating(dto.getRating());
        restaurant.setBusinessHours(dto.getBusinessHours());
        restaurant.setApproved(dto.isApproved());
        restaurant.setDeliveryRange(dto.getDeliveryRange());
        restaurant.setType(dto.getType());
        return restaurant;
    }

    private MenuItemDTO convertToMenuItemDTO(MenuItem menuItem) {
        MenuItemDTO dto = new MenuItemDTO();
        dto.setId(menuItem.getId());
        dto.setName(menuItem.getName());
        dto.setDescription(menuItem.getDescription());
        dto.setPrice(menuItem.getPrice());
        dto.setAvailable(menuItem.isAvailable());
        dto.setCategory(menuItem.getCategory());
        dto.setImageUrl(menuItem.getImageUrl());
        return dto;
    }

    private MenuItem convertToMenuItemEntity(MenuItemDTO dto) {
        MenuItem menuItem = new MenuItem();
        menuItem.setId(dto.getId());
        menuItem.setName(dto.getName());
        menuItem.setDescription(dto.getDescription());
        menuItem.setPrice(dto.getPrice());
        menuItem.setAvailable(dto.isAvailable());
        menuItem.setCategory(dto.getCategory());
        menuItem.setImageUrl(dto.getImageUrl());
        Optional<Restaurant> restaurant = restaurantService.findById(dto.getRestaurantId());
        restaurant.ifPresent(menuItem::setRestaurant);
        return menuItem;
    }

    private CourierDTO convertToCourierDTO(Courier courier) {
        CourierDTO dto = new CourierDTO();
        dto.setId(courier.getId());
        dto.setUsername(courier.getUsername());
        dto.setEmail(courier.getEmail());
        dto.setFirstName(courier.getFirstName());
        dto.setLastName(courier.getLastName());
        dto.setVehicleInfo(courier.getVehicleInfo());
        dto.setAvailable(courier.isAvailable());
        dto.setApproved(courier.isApproved());
        dto.setRating(courier.getRating());
        dto.setType(courier.getType());
        return dto;
    }
}