package com.bbm.backend.models;

import jakarta.persistence.*;
import lombok.Data;

// OrderItem entity (join table with additional attributes)
@Entity
@Table(name = "order_items")
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    private double price;
    private String specialInstructions;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;
}