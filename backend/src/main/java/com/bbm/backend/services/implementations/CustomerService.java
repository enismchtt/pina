package com.bbm.backend.services.implementations;

import com.bbm.backend.exceptions.ResourceNotFoundException;
import com.bbm.backend.models.Customer;
import com.bbm.backend.models.Restaurant;
import com.bbm.backend.repositories.CustomerRepository;
import com.bbm.backend.repositories.RestaurantRepository;
import com.bbm.backend.services.abstractions.ICustomerService;
import com.bbm.backend.util.PasswordUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {

    @Autowired
    private final CustomerRepository customerRepository;

    @Autowired
    private final RestaurantRepository restaurantRepository;


    @Override
    public List<Customer> findByRatingGreaterThanEqual(double rating) {
        return customerRepository.findByRatingGreaterThanEqual(rating);
    }

    @Override
    @Transactional
    public void addFavoriteRestaurant(Long customerId, Long restaurantId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        customer.getFavoriteRestaurants().add(restaurant);
        customerRepository.save(customer);
    }

    @Override
    public void removeFavoriteRestaurant(Long customerId, Long restaurantId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        customer.getFavoriteRestaurants().removeIf(restaurant -> restaurant.getId().equals(restaurantId));
        customerRepository.save(customer);
    }

    @Override
    public Set<Restaurant> getFavoriteRestaurants(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        return customer.getFavoriteRestaurants();
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Optional<Customer> findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return customerRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }
}
