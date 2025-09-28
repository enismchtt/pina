package com.bbm.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public abstract class UserDTO {
    private Long id;
    @JsonProperty("accountType")
    private String type;
    private String username;
    private String email;

    //TODO: Do not use password in DTO structure. The password shouldn't be exposed
    //to the internet and should be hashed.
    private String password;
    private String phoneNumber;
    private boolean active;
    private boolean banned;
    // Important: password is not included in DTO for security reasons
}