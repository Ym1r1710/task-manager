package com.example.taskmanager.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
public class Operation implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String role;

    public Operation() {}

    public Operation(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
