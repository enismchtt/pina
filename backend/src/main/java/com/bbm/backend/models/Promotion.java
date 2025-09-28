package com.bbm.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


// Promotion entity
@Entity
@Table(name = "promotions")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    //Code can be encapsulated by another system
    //like promotion redeem system and its amount can be in a single class
    @EqualsAndHashCode.Include
    private String code;
    private String description;

    //Amount and percantage could be reworked to be only one type
    private double discountAmount;
    private double discountPercentage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    //Active can be a derived bool
    private boolean active;
    private int usageLimit;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant; // Null for platform-wide promotions
}