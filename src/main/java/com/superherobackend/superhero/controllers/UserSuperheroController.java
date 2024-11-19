package com.superherobackend.superhero.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.superherobackend.superhero.models.Superhero;
import com.superherobackend.superhero.repositories.UserSuperheroRepository;
import com.superherobackend.superhero.services.UserSuperheroService;

@RestController
public class UserSuperheroController {

    @Autowired
    private UserSuperheroService userSuperheroService;

    @Autowired
    private UserSuperheroRepository userSuperheroRepository;

    @GetMapping("/users/{userId}/superheroes")
    public ResponseEntity<List<Superhero>> getUserSuperheroes(@PathVariable Long userId) {
        List<Superhero> superheroes = userSuperheroService.getAllSuperheroesByUserId(userId);
        return ResponseEntity.ok(superheroes);
    }

    @PostMapping("/users/{userId}/superheroes/{superheroId}")
    public ResponseEntity<String> addSuperheroToUser(@PathVariable Long userId, @PathVariable Long superheroId) {
        try {
            userSuperheroService.addSuperheroToUser(userId, superheroId);
            return ResponseEntity.status(HttpStatus.OK).body("Superhero added to user successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/users/{userId}/superheroes/{superheroId}")
    public ResponseEntity<String> removeSuperheroFromUser(@PathVariable Long userId, @PathVariable Long superheroId) {
        try {
            userSuperheroService.deleteSuperheroFromUser(userId, superheroId);
            return ResponseEntity.status(HttpStatus.OK).body("Superhero removed from user successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/users/{userId}/superheroes/{superheroId}/exists")
    public ResponseEntity<Boolean> isUserSuperhero(@PathVariable Long userId, @PathVariable Long superheroId) {
        boolean exists = userSuperheroService.isUserSuperhero(userId, superheroId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/superheroes/{superId}")
    public ResponseEntity<Superhero> getSuperheroById(@PathVariable Long superId) {
        Optional<Superhero> superhero = userSuperheroService.getSuperheroById(superId);
        return superhero.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{userId}/superheroes/universe")
    public ResponseEntity<List<Superhero>> getUserSuperheroesByUniverse(@PathVariable Long userId, @RequestParam String universe) {
        List<Superhero> superheroes = userSuperheroRepository.findSuperheroesByUserIdAndUniverse(userId, universe);
        return ResponseEntity.ok(superheroes);
    }

    @GetMapping("/users/{userId}/superheroes/power")
    public ResponseEntity<List<Superhero>> getUserSuperheroesByPower(@PathVariable Long userId, @RequestParam Long powerId) {
        List<Superhero> superheroes = userSuperheroRepository.findSuperheroesByUserIdAndPower(userId, powerId);
        return ResponseEntity.ok(superheroes);
    }
}
