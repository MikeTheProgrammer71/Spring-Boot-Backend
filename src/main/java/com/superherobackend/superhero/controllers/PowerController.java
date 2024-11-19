package com.superherobackend.superhero.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.superherobackend.superhero.models.Power;
import com.superherobackend.superhero.repositories.PowerRepository;

import java.util.List;

@RestController
public class PowerController {

    @Autowired
    private PowerRepository powerRepository;
    
    @GetMapping("/powers")
    public ResponseEntity<List<Power>> getAllPowers() {
        List<Power> powers = powerRepository.findAll();
        return ResponseEntity.ok(powers);
    }

    @GetMapping("/superhero/{superId}")
    public ResponseEntity<List<Power>> getPowersBySuperhero(@PathVariable Long superId) {
        List<Power> powers = powerRepository.findPowersBySuperheroId(superId);                     
        return ResponseEntity.ok(powers);
    }

    @GetMapping("/powers/id")
    public ResponseEntity<Long> getPowerIdByName(@RequestParam String name) {
        Power power = powerRepository.findByName(name);
        if (power != null) {
            return ResponseEntity.ok(power.getPowerId());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/powers/{powerId}/name")
    public ResponseEntity<String> getPowerNameById(@PathVariable Long powerId) {
        Power power = powerRepository.findById(powerId).orElse(null);
        if (power != null) {
            return ResponseEntity.ok(power.getName());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
