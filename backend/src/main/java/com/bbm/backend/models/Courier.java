package com.bbm.backend.models;

import  jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

// Courier entity
@Entity
@Table(name = "couriers")
@Data
@EqualsAndHashCode(callSuper = true)
public class Courier extends User {
    private String firstName;
    private String lastName;
    private String address;
    //vehicleInfo class could be implemented
    private String vehicleInfo;
    private boolean available;
    private boolean approved;
    private double rating;

    @ManyToMany(mappedBy = "registeredCouriers")
    private Set<Restaurant> restaurants;

    @OneToMany(mappedBy = "courier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> deliveries;

    @OneToMany(mappedBy = "courier")
    private List<Review> reviews;

    // Getters and setters
}
