package com.bbm.backend.models;

import com.bbm.backend.utils.enums.ComplaintStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

// Complaint entity
//Complaint and review entities can be inherited b common class
@Entity
@Table(name = "complaints")
@Data
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime complaintTime;
    @Enumerated(EnumType.STRING)
    private ComplaintStatus status;
    private String resolution;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}