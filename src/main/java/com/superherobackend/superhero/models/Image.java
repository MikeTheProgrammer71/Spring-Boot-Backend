package com.superherobackend.superhero.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Column(name = "stored_filename")
    private String storedFilename;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "super_id", referencedColumnName = "super_id", unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Superhero superhero;

}
