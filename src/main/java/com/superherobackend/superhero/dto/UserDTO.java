package com.superherobackend.superhero.dto;

import com.superherobackend.superhero.models.User;

import lombok.Data;

@Data
public class UserDTO {
    private Long userId;
    private String name;
    private String role;
    private String username;
    
    // Get user without password 
    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.role = user.getRole();
        this.username = user.getUsername();
    }
}
