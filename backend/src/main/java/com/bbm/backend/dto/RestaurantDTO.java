package com.bbm.backend.dto;

import com.bbm.backend.utils.enums.CuisineType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RestaurantDTO extends UserDTO {
    private String name;
    private String description;
    private String address;
    private String imageUrl;
    private CuisineType cuisineType;
    private double rating;
    private String businessHours;
    private boolean approved;
    private int deliveryRange;
}
