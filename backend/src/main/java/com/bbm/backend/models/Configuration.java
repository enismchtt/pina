package com.bbm.backend.models;

import jakarta.persistence.*;
import lombok.Data;

// Configuration entity for system-wide settings
@Entity
@Table(name = "configurations")
@Data
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String configKey;
    private String configValue;
    private String description;

    // Getters and setters
}