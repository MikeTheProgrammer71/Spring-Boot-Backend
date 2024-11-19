package com.superherobackend.superhero.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.superherobackend.superhero.dto.SuperheroDTO;
import com.superherobackend.superhero.exceptions.DuplicateSuperheroException;
import com.superherobackend.superhero.models.Superhero;
import com.superherobackend.superhero.services.SuperheroService;

import java.util.List;

@RestController
@RequestMapping("/superheroes")
public class SuperheroController {

    @Autowired
    private SuperheroService superheroService;

    @GetMapping("/all")
    public ResponseEntity<List<Superhero>> getAllSuperheroes() {
        List<Superhero> superheroes = superheroService.getAllSuperheroes();
        return ResponseEntity.ok(superheroes);
    }
    
    @PostMapping("/users/{userId}/superheroes/add")
    public ResponseEntity<?> addNewSuperhero(@PathVariable Long userId,
                                             @RequestParam String name,
                                             @RequestParam String realName,
                                             @RequestParam String universe,
                                             @RequestParam int yearCreated,
                                             @RequestParam Boolean canDelete,
                                             @RequestParam List<Long> powerIds,
                                             @RequestParam MultipartFile image) {
        try {
            SuperheroDTO superheroDTO = new SuperheroDTO(name, realName, universe, yearCreated, canDelete, powerIds);
            Superhero superhero = superheroService.addNewSuperhero(superheroDTO, userId, image);
            return ResponseEntity.ok(superhero);
        } catch (DuplicateSuperheroException e) {
            return ResponseEntity.status(409).body("Superhero already exists for this user.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/max-id")
    public ResponseEntity<Long> getMaxSuperId() {
        try {
            Long maxSuperId = superheroService.getMaxSuperId();
            return ResponseEntity.ok(maxSuperId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(null);
        }
    }

    @GetMapping("/power/{powerId}")
    public ResponseEntity<List<Superhero>> getSuperheroesByPower(@PathVariable Long powerId) {
        List<Superhero> superheroes = superheroService.getSuperheroesByPower(powerId);
        return ResponseEntity.ok(superheroes);
    }

    @DeleteMapping("/{superId}")
    public ResponseEntity<String> deleteSuperhero(@PathVariable Long superId) {
        try {
            superheroService.deleteSuperhero(superId);
            return ResponseEntity.ok("Superhero and all references deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error deleting superhero: " + e.getMessage());
        }
    }
    
}

