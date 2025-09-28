package com.bbm.backend.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long id;
    private int quantity;
    private double price;
    private String specialInstructions;
    private Long menuItemId;
    private String menuItemName;
    private Long orderId;
}