package com.bbm.backend.controllers;

import com.bbm.backend.dto.*;
import com.bbm.backend.models.*;
import com.bbm.backend.repositories.AdministratorRepository;
import com.bbm.backend.services.implementations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final OrderService orderService;
    private final CourierService courierService;
    private final RestaurantService restaurantService;
    private final CustomerService customerService;
    private final ReviewService reviewService;
    private final AdministratorRepository adminRepository;


    @GetMapping("/{id}")
    public ResponseEntity<String> getAdminUsernameById(@PathVariable Long id) {
        return adminRepository.findById(id)
                .map(admin -> ResponseEntity.ok(admin.getUsername()))
                .orElse(ResponseEntity.notFound().build());
    }

    // ==================== User Operations ====================

    @PutMapping("/ban/{id}")
    public ResponseEntity<Void> banUser(@PathVariable Long id) {
        userService.banUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/unban/{id}")
    public ResponseEntity<Void> unbanUser(@PathVariable Long id) {
        userService.unbanUser(id);
        return ResponseEntity.ok().build();
    }

    // ==================== Customer / Courier / Restaurant ====================

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.findAll()
                .stream()
                .map(customer -> {
                    CustomerDTO dto = new CustomerDTO();
                    dto.setId(customer.getId());
                    dto.setUsername(customer.getUsername() != null ? customer.getUsername() : "N/A");
                    dto.setEmail(customer.getEmail() != null ? customer.getEmail() : "N/A");
                    dto.setBanned(customer.isBanned());
                    dto.setFirstName(customer.getFirstName() != null ? customer.getFirstName() : "N/A");
                    dto.setLastName(customer.getLastName() != null ? customer.getLastName() : "N/A");
                    dto.setAddress(customer.getAddress() != null ? customer.getAddress() : "N/A");
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(customers);
    }
    

    @GetMapping("/couriers")
    public ResponseEntity<List<CourierDTO>> getAllCouriers() {
        List<CourierDTO> couriers = courierService.findAll()
                .stream()
                .map(courier -> {
                    CourierDTO dto = new CourierDTO();
                    dto.setId(courier.getId());
                    dto.setUsername(courier.getUsername() != null ? courier.getUsername() : "N/A");
                    dto.setEmail(courier.getEmail() != null ? courier.getEmail() : "N/A");
                    dto.setBanned(courier.isBanned());
                    dto.setFirstName(courier.getFirstName());
                    dto.setLastName(courier.getLastName());
                    dto.setAddress(courier.getAddress());
                    dto.setVehicleInfo(courier.getVehicleInfo());
                    dto.setAvailable(courier.isAvailable());
                    dto.setApproved(courier.isApproved());
                    dto.setRating(courier.getRating());
                    dto.setPassword("••••••"); // şifre koyma kararı
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(couriers);
    }

    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
        List<RestaurantDTO> restaurants = restaurantService.findAll()
                .stream()
                .map(restaurant -> {
                    RestaurantDTO dto = new RestaurantDTO();
                    dto.setId(restaurant.getId());
                    dto.setUsername(restaurant.getUsername() != null ? restaurant.getUsername() : "N/A");
                    dto.setEmail(restaurant.getEmail() != null ? restaurant.getEmail() : "N/A");
                    dto.setBanned(restaurant.isBanned());
                    dto.setName(restaurant.getName());
                    dto.setDescription(restaurant.getDescription());
                    dto.setAddress(restaurant.getAddress());
                    dto.setCuisineType(restaurant.getCuisineType());
                    dto.setRating(restaurant.getRating());
                    dto.setBusinessHours(restaurant.getBusinessHours());
                    dto.setApproved(restaurant.isApproved());
                    dto.setDeliveryRange(restaurant.getDeliveryRange());
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/restaurants/{id}")
    public ResponseEntity<RestaurantDTO> getRestaurant(@PathVariable Long id) {
        return restaurantService.findById(id)
            .map(restaurant -> {
                RestaurantDTO dto = new RestaurantDTO();
                dto.setId(restaurant.getId());
                dto.setUsername(restaurant.getUsername());
                dto.setEmail(restaurant.getEmail());
                dto.setBanned(restaurant.isBanned());
                dto.setName(restaurant.getName());
                dto.setDescription(restaurant.getDescription());
                dto.setAddress(restaurant.getAddress());
                dto.setCuisineType(restaurant.getCuisineType());
                dto.setRating(restaurant.getRating());
                dto.setBusinessHours(restaurant.getBusinessHours());
                dto.setApproved(restaurant.isApproved());
                dto.setDeliveryRange(restaurant.getDeliveryRange());
                return ResponseEntity.ok(dto);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/couriers/{id}")
    public ResponseEntity<CourierDTO> getCourier(@PathVariable Long id) {
        return courierService.findById(id)
            .map(courier -> {
                CourierDTO dto = new CourierDTO();
                dto.setId(courier.getId());
                dto.setUsername(courier.getUsername());
                dto.setEmail(courier.getEmail());
                dto.setBanned(courier.isBanned());
                dto.setFirstName(courier.getFirstName());
                dto.setLastName(courier.getLastName());
                dto.setAddress(courier.getAddress());
                dto.setVehicleInfo(courier.getVehicleInfo());
                dto.setAvailable(courier.isAvailable());
                dto.setApproved(courier.isApproved());
                dto.setRating(courier.getRating());
                return ResponseEntity.ok(dto);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // ==================== Reviews and Orders ====================

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<ReviewDTO> reviews = reviewService.findAll()
                .stream()
                .map(review -> {
                    ReviewDTO dto = new ReviewDTO();
                    dto.setId(review.getId());
                    dto.setRating(review.getRating());
                    dto.setComment(review.getComment());
                    dto.setReviewTime(review.getReviewTime());
                    dto.setReviewType(review.getReviewType());
                    dto.setCustomerId(review.getCustomer() != null ? review.getCustomer().getId() : null);
                    dto.setRestaurantId(review.getRestaurant() != null ? review.getRestaurant().getId() : null);
                    dto.setCourierId(review.getCourier() != null ? review.getCourier().getId() : null);
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.findAll()
                .stream()
                .map(order -> {
                    OrderDTO dto = new OrderDTO();
                    dto.setId(order.getId());
                    dto.setOrderTime(order.getOrderTime());
                    dto.setOrderStatus(order.getOrderStatus());
                    dto.setTotalAmount(order.getTotalAmount());
                    dto.setDeliveryAddress(order.getDeliveryAddress());
                    dto.setPaymentMethod(order.getPaymentMethod());
                    dto.setPaymentStatus(order.getPaymentStatus());
                    dto.setDeliveryTime(order.getDeliveryTime());
                    dto.setSpecialInstructions(order.getSpecialInstructions());
                    dto.setCustomerId(order.getCustomer() != null ? order.getCustomer().getId() : null);
                    dto.setRestaurantId(order.getRestaurant() != null ? order.getRestaurant().getId() : null);
                    dto.setCourierId(order.getCourier() != null ? order.getCourier().getId() : null);
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(orders);
    }

    // ==================== Pending Approvals ====================

    @GetMapping("/pending-couriers")
    public ResponseEntity<List<CourierDTO>> getPendingCouriers() {
    List<CourierDTO> pendingCouriers = courierService.findAll()
            .stream()
            .filter(courier -> !courier.isApproved()).map(this::toCourierDTO)
            .toList();
    return ResponseEntity.ok(pendingCouriers);
    }

    @PutMapping("/approve-courier/{id}")
    public ResponseEntity<Void> approveCourier(@PathVariable Long id) {
        courierService.approveCourier(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reject-courier/{id}")
    public ResponseEntity<Void> rejectCourier(@PathVariable Long id) {
        courierService.deleteById(id); 
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/pending-restaurants")
    public ResponseEntity<List<RestaurantDTO>> getPendingRestaurants() {
        List<RestaurantDTO> pendingRestaurants = restaurantService.findAll()
                .stream()
                .filter(restaurant -> !restaurant.isApproved()).map(this::toRestaurantDTO)
                .toList();
        return ResponseEntity.ok(pendingRestaurants);
    }

    @PutMapping("/approve-restaurant/{id}")
    public ResponseEntity<Void> approveRestaurant(@PathVariable Long id) {
        restaurantService.approveRestaurant(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reject-restaurant/{id}")
    public ResponseEntity<Void> rejectRestaurant(@PathVariable Long id) {
        restaurantService.deleteById(id); 
        return ResponseEntity.noContent().build();
    }
    private CourierDTO toCourierDTO(Courier c) {
        CourierDTO dto = new CourierDTO();
        dto.setId(c.getId());
        dto.setUsername(c.getUsername());
        dto.setPassword(c.getPassword());
        dto.setEmail(c.getEmail());
        dto.setPhoneNumber(c.getPhoneNumber());
        dto.setAddress(c.getAddress());
        dto.setFirstName(c.getFirstName());
        dto.setLastName(c.getLastName());
        dto.setVehicleInfo(c.getVehicleInfo());
        dto.setAvailable(c.isAvailable());
        dto.setApproved(c.isApproved());
        dto.setRating(c.getRating());
        dto.setType(c.getType());
        dto.setBanned(c.isBanned());
        return dto;
    }
    private RestaurantDTO toRestaurantDTO(Restaurant restaurant) {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setId(restaurant.getId());
        dto.setUsername(restaurant.getUsername());
        dto.setPassword(restaurant.getPassword());
        dto.setEmail(restaurant.getEmail());
        dto.setPhoneNumber(restaurant.getPhoneNumber());
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

}