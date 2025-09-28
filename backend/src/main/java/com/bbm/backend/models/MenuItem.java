package com.bbm.backend.models;
import com.bbm.backend.utils.enums.CuisineType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

// MenuItem entity
@Entity
@Table(name = "menu_items")
@Data
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private boolean available;
    @Enumerated(EnumType.STRING)
    private CuisineType category;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "menuItem")
    private List<OrderItem> orderItems;

    // Getters and setters
}