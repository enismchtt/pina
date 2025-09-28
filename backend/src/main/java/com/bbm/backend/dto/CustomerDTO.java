package com.bbm.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDTO extends UserDTO {
    private String firstName;
    private String lastName;
    private String address;
    private double rating;
}
