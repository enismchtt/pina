package com.bbm.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

// Customer entity
@Entity
@Table(name = "customers")
@Data
@EqualsAndHashCode(callSuper = true)
public class Customer extends User {
    private String firstName;
    private String lastName;
    private String address;
    private double rating;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    @OneToMany(mappedBy = "customer")
    private List<Review> reviews;

    @ManyToMany
    @JoinTable(
            name = "customer_favorite_restaurants",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "restaurant_id")
    )
    private Set<Restaurant> favoriteRestaurants;

    // Getters and setters
}
