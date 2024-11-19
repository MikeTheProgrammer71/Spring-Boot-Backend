package com.superherobackend.superhero.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "superheroes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Superhero implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "super_id")
    private Long superId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "real_name")
    private String realName;

    @Column(name = "universe")
    private String universe;

    @Column(name = "year_created")
    private Integer yearCreated;

    @Column(name = "can_delete")
    private Boolean canDelete;

    @OneToOne(
        mappedBy = "superhero", 
        cascade = CascadeType.ALL, 
        fetch = FetchType.LAZY, 
        optional = true
    )
    private Image image;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "superhero_powers",
        joinColumns = @JoinColumn(name = "super_id"),
        inverseJoinColumns = @JoinColumn(name = "power_id")
    )
    private Set<Power> powers;
}