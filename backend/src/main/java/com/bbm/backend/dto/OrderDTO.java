package com.bbm.backend.dto;

import com.bbm.backend.utils.enums.PaymentMethod;
import com.bbm.backend.utils.enums.PaymentStatus;
import com.bbm.backend.utils.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private Long id;
    private LocalDateTime orderTime;
    private OrderStatus orderStatus;
    private double totalAmount;
    private String deliveryAddress;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime deliveryTime;
    private String specialInstructions;
    private String customerName;
    private String restaurantName;
    private Long customerId;
    private Long restaurantId;
    private Long courierId;
}