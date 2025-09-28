package com.bbm.backend.controllers;

import com.bbm.backend.dto.CustomerDTO;
import com.bbm.backend.dto.RestaurantDTO;
import com.bbm.backend.models.Customer;
import com.bbm.backend.models.Restaurant;
import com.bbm.backend.services.implementations.CustomerService;
import com.bbm.backend.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:3000") // Add this line
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        return customerService.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        if (customerService.existsByUsername(customerDTO.getUsername())) {
            return ResponseEntity.badRequest().build();
        }
        customerDTO.setType("customer");
        Customer customer = convertToEntity(customerDTO);
        customer.setPassword(PasswordUtil.encode(customer.getPassword()));
        Customer savedCustomer = customerService.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedCustomer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        if (!customerService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Customer customer = convertToEntity(customerDTO);
        customer.setId(id);
        customer.setPassword(PasswordUtil.encode(customer.getPassword()));
        Customer updatedCustomer = customerService.save(customer);
        return ResponseEntity.ok(convertToDTO(updatedCustomer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        if (!customerService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/favorite-restaurants")
    public ResponseEntity<Set<RestaurantDTO>> getFavoriteRestaurants(@PathVariable Long id) {
        if (!customerService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Set<RestaurantDTO> favorites = customerService.getFavoriteRestaurants(id).stream()
                .map(this::convertToRestaurantDTO)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(favorites);
    }

    @PostMapping("/{id}/favorite-restaurants/{restaurantId}")
    public ResponseEntity<Void> addFavoriteRestaurant(@PathVariable Long id, @PathVariable Long restaurantId) {
        if (customerService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        customerService.addFavoriteRestaurant(id, restaurantId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/favorite-restaurants/{restaurantId}")
    public ResponseEntity<Void> removeFavoriteRestaurant(@PathVariable Long id, @PathVariable Long restaurantId) {
        if (customerService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        customerService.removeFavoriteRestaurant(id, restaurantId);
        return ResponseEntity.noContent().build();
    }

    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setUsername(customer.getUsername());
        dto.setPassword(customer.getPassword());
        dto.setEmail(customer.getEmail());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setActive(customer.isActive());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setAddress(customer.getAddress());
        dto.setRating(customer.getRating());
        dto.setType(customer.getType());
        dto.setBanned(customer.isBanned());
        return dto;
    }

    private Customer convertToEntity(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setId(dto.getId());
        customer.setUsername(dto.getUsername());
        customer.setPassword(dto.getPassword());
        customer.setEmail(dto.getEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setActive(dto.isActive());
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setAddress(dto.getAddress());
        customer.setRating(dto.getRating());
        customer.setType(dto.getType());
        return customer;
    }

    private RestaurantDTO convertToRestaurantDTO(Restaurant restaurant) {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setDescription(restaurant.getDescription());
        dto.setAddress(restaurant.getAddress());
        dto.setCuisineType(restaurant.getCuisineType());
        dto.setRating(restaurant.getRating());
        dto.setApproved(restaurant.isApproved());
        dto.setType(restaurant.getType());
        return dto;
    }
}