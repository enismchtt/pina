package com.bbm.backend.models;

import com.bbm.backend.utils.enums.OrderStatus;
import com.bbm.backend.utils.enums.PaymentMethod;
import com.bbm.backend.utils.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

// Order entity
@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderTime;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private double totalAmount;
    private String deliveryAddress;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    private LocalDateTime deliveryTime;
    private String specialInstructions;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    // Getters and setters
}