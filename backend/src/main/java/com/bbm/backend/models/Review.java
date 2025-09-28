package com.bbm.backend.models;


import com.bbm.backend.utils.enums.ReviewType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

// Review entity
@Entity
@Table(name = "reviews")
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating;
    private String comment;
    private LocalDateTime reviewTime;

    @Enumerated(EnumType.STRING)
    private ReviewType reviewType;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "courier_id")
    private Courier courier;

}
