package com.superherobackend.superhero.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuperheroDTO {
    private String name;
    private String realName;
    private String universe;
    private int yearCreated;
    private Boolean canDelete;
    private List<Long> powerIds;
    private MultipartFile image;

    public SuperheroDTO(String name, String realName, String universe, int yearCreated, Boolean canDelete, List<Long> powerIds) {
        this.name = name;
        this.realName = realName;
        this.universe = universe;
        this.yearCreated = yearCreated;
        this.canDelete = canDelete;
        this.powerIds = powerIds;
    }
}
