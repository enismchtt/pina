package com.bbm.backend.models;

import com.bbm.backend.utils.enums.CuisineType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;


// Restaurant entity
@Entity
@Table(name = "restaurants")
@Data
@EqualsAndHashCode(callSuper = true)
public class Restaurant extends User {
    private String name;
    private String description;
    private String address;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private CuisineType cuisineType;
    private double rating;

    //Should be other than string
    private String businessHours;
    private boolean approved;
    private int deliveryRange;


    // Could be restaurant <-> menu <-> menuItem
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<MenuItem> menuItems;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    @ManyToMany
    @JoinTable(
            name = "restaurant_couriers",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "courier_id")
    )
    private Set<Courier> registeredCouriers;

    @OneToMany(mappedBy = "restaurant")
    private List<Review> reviews;

    // Getters and setters
}