package com.bbm.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CourierDTO extends UserDTO {
    private String firstName;
    private String lastName;
    private String address;
    private String vehicleInfo;
    private boolean available;
    private boolean approved;
    private double rating;
}