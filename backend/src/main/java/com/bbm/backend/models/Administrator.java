package com.bbm.backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Administrator entity
@Entity
@Table(name = "administrators")
@Data
@EqualsAndHashCode(callSuper = true)
public class Administrator extends User {
    private String adminRole;

    // Getters and setters
}
