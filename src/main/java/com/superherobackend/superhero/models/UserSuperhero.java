package com.superherobackend.superhero.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_superheroes")
@Getter
@Setter
@NoArgsConstructor
public class UserSuperhero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "super_id")
    private Superhero superhero;

    public UserSuperhero(User user, Superhero superhero) {
        this.user = user;
        this.superhero = superhero;
    }
    
}
