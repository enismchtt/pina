package com.bbm.backend.dto;

import com.bbm.backend.utils.enums.CuisineType;
import lombok.Data;

@Data
public class MenuItemDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private boolean available;
    private CuisineType category;
    private String imageUrl;
    private Long restaurantId;
}