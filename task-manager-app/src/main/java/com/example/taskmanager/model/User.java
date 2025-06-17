package com.example.taskmanager.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String fullName;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Operation> roles = new ArrayList<>();

    public User() {}

    public User(String username, String fullName, String password, List<Operation> roles) {
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.roles = roles;
    }

    public User(Long id, String username, String fullName, String password, List<Operation> roles) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public List<Operation> getRoles() { return roles; }
    public void setRoles(List<Operation> roles) { this.roles = roles; }
}