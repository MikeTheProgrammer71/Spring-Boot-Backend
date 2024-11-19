package com.superherobackend.superhero.models;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")
@Data                 // @Data is getters, setters, toString, equals, and hashCode
@NoArgsConstructor     
@AllArgsConstructor    
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;

    private String role;

    private String username;

    private String hashedPassword;
}

