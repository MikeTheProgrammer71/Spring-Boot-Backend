package com.superherobackend.superhero.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.superherobackend.superhero.models.Power;

import java.util.List;

@Repository
public interface PowerRepository extends CrudRepository<Power, Long> {
    List<Power> findAll();

    Power findByName(String name);

    @Query("SELECT p FROM Power p JOIN p.superheroes s WHERE s.superId = :superId")
    List<Power> findPowersBySuperheroId(Long superId);
}
