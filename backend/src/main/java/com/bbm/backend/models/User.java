package com.bbm.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
@Data
/*
 * because password and is active can be changed repeatedly and doesn't distinguish any user they are excluded
 * if any field added or deleted effects the uniqueness of the user they should be considered to be excluded
 * */
@EqualsAndHashCode(exclude = {"password", "active"})
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private String username;

    //Should be protected by hashcoding the password
    private String password;
    private String type;
    private String email;
    private String phoneNumber;
    private boolean active;

    @Column(nullable = false)
    private boolean banned = false;
    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }
}
